package com.mastersessay.blockchain.accounting.service;

import com.mastersessay.blockchain.accounting.consts.OrderStatus;
import com.mastersessay.blockchain.accounting.dto.request.order.OrderActionHistoryDto;
import com.mastersessay.blockchain.accounting.dto.request.order.OrderProcessingRequest;
import com.mastersessay.blockchain.accounting.dto.request.order.OrderRequest;
import com.mastersessay.blockchain.accounting.dto.response.order.*;
import com.mastersessay.blockchain.accounting.model.order.*;
import com.mastersessay.blockchain.accounting.model.user.User;
import com.mastersessay.blockchain.accounting.repository.*;
import com.mastersessay.blockchain.accounting.util.PageUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.mastersessay.blockchain.accounting.consts.BlockchainAccountingConstants.BusinessMessages.*;
import static com.mastersessay.blockchain.accounting.consts.BlockchainAccountingConstants.Security.USER_NOT_FOUND_WITH_USERNAME_ERROR_MESSAGE;

@Service
@SuppressWarnings("Duplicates")
public class OrderService {
    private static final Logger log = LoggerFactory.getLogger(GenericCrudProcessor.class);

    private final AirHandlingUnitRepository airHandlingUnitRepository;
    private final AirConditioningDeviceRepository airConditioningDeviceRepository;
    private final FanRepository fanRepository;
    private final MiningCoolingRackRepository miningCoolingRackRepository;
    private final MiningFarmRepository miningFarmRepository;

    private final UserRepository userRepository;
    private final PageUtils pageUtils;

    private final MiningFarmService miningFarmService;
    private final MiningCoolingRackService miningCoolingRackService;
    private final AirConditioningDeviceService airConditioningDeviceService;
    private final AirHandlingUnitService airHandlingUnitService;
    private final FanService fanService;

    private final OrderRepository orderRepository;
    private final OrderAirConditioningDeviceRepository orderAirConditioningDeviceRepository;
    private final OrderAirHandlingUnitRepository orderAirHandlingUnitRepository;
    private final OrderFanRepository orderFanRepository;
    private final OrderMiningCoolingRackRepository orderMiningCoolingRackRepository;
    private final OrderMiningFarmRepository orderMiningFarmRepository;
    private final OrdersActionHistoryRepository ordersActionHistoryRepository;

    @Transactional
    public List<OrderResponse> getAll(Integer start,
                                      Integer count,
                                      String sortBy,
                                      String sortType) {
        PageRequest pageRequest = pageUtils.formPageRequest(start, count, sortBy, sortType);
        Page<Order> pageResult = orderRepository.findAll(pageRequest);

        List<OrderResponse> orderResponse = new ArrayList<>();

        if (pageResult.hasContent()) {
            pageResult
                    .getContent()
                    .forEach(order -> orderResponse.add(formOrderResponseFromEntity(order)));

            return orderResponse;
        } else {
            return new ArrayList<>();
        }
    }

    @Transactional
    public List<OrderResponse> getAllByUsername(String usernameWaitingFor,
                                                Integer start,
                                                Integer count,
                                                String sortBy,
                                                String sortType) {
        PageRequest pageRequest = pageUtils.formPageRequest(start, count, sortBy, sortType);
        Page<Order> pageResult = orderRepository.getByWaitingActionUsername(usernameWaitingFor, pageRequest);

        List<OrderResponse> orderResponse = new ArrayList<>();

        if (pageResult.hasContent()) {
            pageResult
                    .getContent()
                    .forEach(order -> orderResponse.add(formOrderResponseFromEntity(order)));

            return orderResponse;
        } else {
            return new ArrayList<>();
        }
    }

    @Transactional
    public OrderResponse initiateOrder(OrderRequest orderRequest, UserDetails userDetails) {
        Order order = Order
                .builder()
                .createdBy(userDetails.getUsername())
                .createdWhen(LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy hh:mm")))
                .status(orderRequest.getStatus())
                .orderType(orderRequest.getOrderType())
                .waitingActionUsername(orderRequest.getWaitingActionUsername())
                .build();

        List<OrderMiningFarm> orderMiningFarms = new ArrayList<>();
        List<OrderMiningCoolingRack> orderMiningCoolingRacks = new ArrayList<>();
        List<OrderAirConditioningDevice> orderAirConditioningDevices = new ArrayList<>();
        List<OrderAirHandlingUnit> orderAirHandlingUnits = new ArrayList<>();
        List<OrderFan> orderFanDs = new ArrayList<>();
        List<OrdersActionHistory> ordersActionHistories = new ArrayList<>();

        Order savedOrder = orderRepository.save(order);

        orderRequest
                .getOrderMiningFarms()
                .forEach(miningFarm -> orderMiningFarms.add(OrderMiningFarm
                        .builder()
                        .order(savedOrder)
                        .amount(miningFarm.getAmount())
                        .miningFarm(miningFarmRepository.getById(miningFarm.getMiningFarmId()).get())
                        .orderDevicePurpose(miningFarm.getOrderDevicePurpose())
                        .build()));

        orderRequest
                .getOrderMiningCoolingRacks()
                .forEach(miningCoolingRack -> orderMiningCoolingRacks.add(OrderMiningCoolingRack
                        .builder()
                        .order(savedOrder)
                        .amount(miningCoolingRack.getAmount())
                        .miningCoolingRack(miningCoolingRackRepository.getById(miningCoolingRack.getMiningCoolingRackId()).get())
                        .orderDevicePurpose(miningCoolingRack.getOrderDevicePurpose())
                        .build()));

        orderRequest
                .getOrderFanDs()
                .forEach(fan -> orderFanDs.add(OrderFan
                        .builder()
                        .order(savedOrder)
                        .amount(fan.getAmount())
                        .fan(fanRepository.getById(fan.getFanId()).get())
                        .orderDevicePurpose(fan.getOrderDevicePurpose())
                        .build()));

        orderRequest
                .getOrderFanDs()
                .forEach(airConditioningDevice -> orderAirConditioningDevices.add(OrderAirConditioningDevice
                        .builder()
                        .order(savedOrder)
                        .amount(airConditioningDevice.getAmount())
                        .airConditioningDevice(airConditioningDeviceRepository.getById(airConditioningDevice.getFanId()).get())
                        .orderDevicePurpose(airConditioningDevice.getOrderDevicePurpose())
                        .build()));

        orderRequest
                .getOrderFanDs()
                .forEach(airHandlingUnit -> orderAirHandlingUnits.add(OrderAirHandlingUnit
                        .builder()
                        .order(savedOrder)
                        .amount(airHandlingUnit.getAmount())
                        .airHandlingUnit(airHandlingUnitRepository.getById(airHandlingUnit.getFanId()).get())
                        .orderDevicePurpose(airHandlingUnit.getOrderDevicePurpose())
                        .build()));

        ordersActionHistories.add(OrdersActionHistory
                .builder()
                .order(savedOrder)
                .statusFrom(null)
                .statusTo(OrderStatus.PLANNED)
                .actionExecutingDate(LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy hh:mm")))
                .actionExecutionUsername(userDetails.getUsername())
                .actionComment(orderRequest.getActionComment())
                .build());

        savedOrder.setName(savedOrder.getOrderType() + " " + savedOrder.getOrderId());
        savedOrder.setOrderMiningFarms(orderMiningFarms);
        savedOrder.setOrderMiningCoolingRacks(orderMiningCoolingRacks);
        savedOrder.setOrderFans(orderFanDs);
        savedOrder.setOrderAirConditioningDevices(orderAirConditioningDevices);
        savedOrder.setOrderAirHandlingUnits(orderAirHandlingUnits);
        savedOrder.setOrdersActionHistoryItems(ordersActionHistories);

        orderMiningFarmRepository.saveAll(orderMiningFarms);
        orderMiningCoolingRackRepository.saveAll(orderMiningCoolingRacks);
        orderFanRepository.saveAll(orderFanDs);
        orderAirConditioningDeviceRepository.saveAll(orderAirConditioningDevices);
        orderAirHandlingUnitRepository.saveAll(orderAirHandlingUnits);
        ordersActionHistoryRepository.saveAll(ordersActionHistories);

        Order namedOrder = orderRepository.save(savedOrder);

        return formOrderResponseFromEntity(namedOrder);
    }

    @Transactional
    public OrderResponse updateOrder(Long orderId,
                                     OrderRequest orderRequest,
                                     UserDetails userDetails) {
        Order foundOrder = orderRepository
                .findById(orderId)
                .orElseThrow(() -> new EntityNotFoundException(OBJECT_NOT_FOUND_MESSAGE));

        if (foundOrder.getStatus().equals(OrderStatus.CANCELLED)
                || foundOrder.getStatus().equals(OrderStatus.COMPLETED)) {
            throw new IllegalArgumentException(ORDER_STATUS_FOR_UPDATE_FINISHED_OR_CANCELLED);
        } else {
            foundOrder.setOrderType(orderRequest.getOrderType());
            foundOrder.setWaitingActionUsername(orderRequest.getWaitingActionUsername());
            foundOrder.setModifiedBy(userDetails.getUsername());
            foundOrder.setModifiedWhen(LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy hh:mm")));

            List<OrderMiningFarm> orderMiningFarms = foundOrder.getOrderMiningFarms();
            List<OrderMiningCoolingRack> orderMiningCoolingRacks = foundOrder.getOrderMiningCoolingRacks();
            List<OrderAirConditioningDevice> orderAirConditioningDevices = foundOrder.getOrderAirConditioningDevices();
            List<OrderAirHandlingUnit> orderAirHandlingUnits = foundOrder.getOrderAirHandlingUnits();
            List<OrderFan> orderFanDs = foundOrder.getOrderFans();

            orderMiningFarms.clear();
            orderMiningCoolingRacks.clear();
            orderAirConditioningDevices.clear();
            orderAirHandlingUnits.clear();
            orderFanDs.clear();

            Order savedUpdatedOrder = orderRepository.save(foundOrder);

            orderRequest
                    .getOrderMiningFarms()
                    .forEach(miningFarm -> {
                        if (!miningFarm.getToDeleteFromOrder()) {
                            orderMiningFarms.add(OrderMiningFarm
                                    .builder()
                                    .order(savedUpdatedOrder)
                                    .amount(miningFarm.getAmount())
                                    .miningFarm(miningFarmRepository.getById(miningFarm.getMiningFarmId()).get())
                                    .orderDevicePurpose(miningFarm.getOrderDevicePurpose())
                                    .build());
                        }
                    });

            orderRequest
                    .getOrderMiningCoolingRacks()
                    .forEach(miningCoolingRack -> {
                        if (!miningCoolingRack.getToDeleteFromOrder()) {
                            orderMiningCoolingRacks.add(OrderMiningCoolingRack
                                    .builder()
                                    .order(savedUpdatedOrder)
                                    .amount(miningCoolingRack.getAmount())
                                    .miningCoolingRack(miningCoolingRackRepository.getById(miningCoolingRack.getMiningCoolingRackId()).get())
                                    .orderDevicePurpose(miningCoolingRack.getOrderDevicePurpose())
                                    .build());
                        }
                    });

            orderRequest
                    .getOrderFanDs()
                    .forEach(fan -> {
                        if (!fan.getToDeleteFromOrder()) {
                            orderFanDs.add(OrderFan
                                    .builder()
                                    .order(savedUpdatedOrder)
                                    .amount(fan.getAmount())
                                    .fan(fanRepository.getById(fan.getFanId()).get())
                                    .orderDevicePurpose(fan.getOrderDevicePurpose())
                                    .build());
                        }
                    });
            orderRequest
                    .getOrderFanDs()
                    .forEach(airConditioningDevice -> {
                        if (!airConditioningDevice.getToDeleteFromOrder()) {
                            orderAirConditioningDevices.add(OrderAirConditioningDevice
                                    .builder()
                                    .order(savedUpdatedOrder)
                                    .amount(airConditioningDevice.getAmount())
                                    .airConditioningDevice(airConditioningDeviceRepository.getById(airConditioningDevice.getFanId()).get())
                                    .orderDevicePurpose(airConditioningDevice.getOrderDevicePurpose())
                                    .build());
                        }
                    });

            orderRequest
                    .getOrderFanDs()
                    .forEach(airHandlingUnit -> {
                        if (!airHandlingUnit.getToDeleteFromOrder()) {
                            orderAirHandlingUnits.add(OrderAirHandlingUnit
                                    .builder()
                                    .order(savedUpdatedOrder)
                                    .amount(airHandlingUnit.getAmount())
                                    .airHandlingUnit(airHandlingUnitRepository.getById(airHandlingUnit.getFanId()).get())
                                    .orderDevicePurpose(airHandlingUnit.getOrderDevicePurpose())
                                    .build());
                        }
                    });

            savedUpdatedOrder.setOrderMiningFarms(orderMiningFarms);
            savedUpdatedOrder.setOrderMiningCoolingRacks(orderMiningCoolingRacks);
            savedUpdatedOrder.setOrderFans(orderFanDs);
            savedUpdatedOrder.setOrderAirConditioningDevices(orderAirConditioningDevices);
            savedUpdatedOrder.setOrderAirHandlingUnits(orderAirHandlingUnits);

            orderMiningFarmRepository.saveAll(orderMiningFarms);
            orderMiningCoolingRackRepository.saveAll(orderMiningCoolingRacks);
            orderFanRepository.saveAll(orderFanDs);
            orderAirConditioningDeviceRepository.saveAll(orderAirConditioningDevices);
            orderAirHandlingUnitRepository.saveAll(orderAirHandlingUnits);

            Order namedOrder = orderRepository.save(savedUpdatedOrder);

            return formOrderResponseFromEntity(namedOrder);
        }
    }

    @Transactional
    public OrderResponse updateOrderStatus(Long orderId,
                                           OrderProcessingRequest orderProcessingRequest,
                                           UserDetails userDetails) {
        Order foundOrder = orderRepository
                .findById(orderId)
                .orElseThrow(() -> new EntityNotFoundException(OBJECT_NOT_FOUND_MESSAGE));

        if (foundOrder.getStatus().equals(orderProcessingRequest.getStatusTo())
                || foundOrder.getStatus().equals(orderProcessingRequest.getStatusFrom())) {
            throw new IllegalArgumentException(ORDER_STATUS_FOR_UPDATE_INAPPROPRIATE);
        }

        if (foundOrder.getStatus().equals(OrderStatus.CANCELLED)
                || foundOrder.getStatus().equals(OrderStatus.COMPLETED)) {
            throw new IllegalArgumentException(ORDER_STATUS_FOR_UPDATE_FINISHED_OR_CANCELLED);
        }

        List<OrdersActionHistory> ordersActionHistoryItems = foundOrder.getOrdersActionHistoryItems();

        User orderAssignedUser = userRepository
                .findByUsername(orderProcessingRequest.getWaitingActionUsername())
                .orElseThrow(() -> new EntityNotFoundException(USER_NOT_FOUND_WITH_USERNAME_ERROR_MESSAGE));

        ordersActionHistoryItems.add(OrdersActionHistory
                .builder()
                .statusFrom(foundOrder.getStatus())
                .statusTo(orderProcessingRequest.getStatusTo())
                .actionComment(orderProcessingRequest.getActionComment())
                .actionExecutingDate(LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy hh:mm")))
                .order(foundOrder)
                .actionExecutionUsername(userDetails.getUsername())
                .build());

        ordersActionHistoryRepository.saveAll(ordersActionHistoryItems);

        foundOrder.setStatus(orderProcessingRequest.getStatusTo());
        foundOrder.setWaitingActionUsername(orderAssignedUser.getUsername());
        Order updatedOrder = orderRepository.save(foundOrder);

        return formOrderResponseFromEntity(updatedOrder);
    }

    private OrderResponse formOrderResponseFromEntity(Order order) {
        return OrderResponse
                .builder()
                .orderId(order.getOrderId())
                .name(order.getName())
                .status(order.getStatus())
                .orderType(order.getOrderType())
                .waitingActionUsername(StringUtils.isBlank(order.getWaitingActionUsername()) ? "" : order.getWaitingActionUsername())
                .orderActionHistory(order
                        .getOrdersActionHistoryItems()
                        .stream()
                        .map(historyItem -> OrderActionHistoryDto
                                .builder()
                                .id(historyItem.getId())
                                .actionExecutingDate(historyItem.getActionExecutingDate())
                                .statusFrom(historyItem.getStatusFrom() == null ? historyItem.getStatusFrom() : historyItem.getStatusFrom())
                                .statusTo(historyItem.getStatusTo())
                                .actionComment(historyItem.getActionComment())
                                .actionExecutionUsername(historyItem.getActionExecutionUsername())
                                .build()
                        )
                        .collect(Collectors.toList())
                )
                .orderMiningFarms(order
                        .getOrderMiningFarms()
                        .stream()
                        .map(orderMiningFarm -> OrderMiningFarmResponse
                                .builder()
                                .amount(orderMiningFarm.getAmount())
                                .miningFarm(miningFarmService.getById(orderMiningFarm.getMiningFarm().getId()))
                                .orderDevicePurpose(orderMiningFarm.getOrderDevicePurpose())
                                .build()
                        )
                        .collect(Collectors.toList())
                )
                .orderMiningCoolingRacks(order
                        .getOrderMiningCoolingRacks()
                        .stream()
                        .map(orderMiningCoolingRack -> OrderMiningCoolingRackResponse
                                .builder()
                                .amount(orderMiningCoolingRack.getAmount())
                                .miningCooling(miningCoolingRackService.getById(orderMiningCoolingRack.getMiningCoolingRack().getId()))
                                .orderDevicePurpose(orderMiningCoolingRack.getOrderDevicePurpose())
                                .build()
                        )
                        .collect(Collectors.toList())
                )
                .orderAirConditioningDevices(order
                        .getOrderAirConditioningDevices()
                        .stream()
                        .map(airConditioningDevice -> OrderAirConditioningDeviceResponse
                                .builder()
                                .amount(airConditioningDevice.getAmount())
                                .airConditioningDevice(airConditioningDeviceService.getById(airConditioningDevice.getAirConditioningDevice().getId()))
                                .orderDevicePurpose(airConditioningDevice.getOrderDevicePurpose())
                                .build()
                        )
                        .collect(Collectors.toList())
                )
                .orderAirHandlingUnits(order
                        .getOrderAirHandlingUnits()
                        .stream()
                        .map(airHandlingUnit -> OrderAirHandlingUnitResponse
                                .builder()
                                .amount(airHandlingUnit.getAmount())
                                .airHandlingUnit(airHandlingUnitService.getById(airHandlingUnit.getAirHandlingUnit().getId()))
                                .orderDevicePurpose(airHandlingUnit.getOrderDevicePurpose())
                                .build()
                        )
                        .collect(Collectors.toList())
                )
                .orderFanDs(order
                        .getOrderFans()
                        .stream()
                        .map(orderFan -> OrderFanResponse
                                .builder()
                                .amount(orderFan.getAmount())
                                .fan(fanService.getById(orderFan.getFan().getId()))
                                .orderDevicePurpose(orderFan.getOrderDevicePurpose())
                                .build()
                        )
                        .collect(Collectors.toList())
                )
                .build();
    }

    @Autowired
    public OrderService(AirHandlingUnitRepository airHandlingUnitRepository, AirConditioningDeviceRepository airConditioningDeviceRepository, FanRepository fanRepository, MiningCoolingRackRepository miningCoolingRackRepository, MiningFarmRepository miningFarmRepository, UserRepository userRepository, PageUtils pageUtils, MiningFarmService miningFarmService, MiningCoolingRackService miningCoolingRackService, AirConditioningDeviceService airConditioningDeviceService, AirHandlingUnitService airHandlingUnitService, FanService fanService, OrderRepository orderRepository, OrderAirConditioningDeviceRepository orderAirConditioningDeviceRepository, OrderAirHandlingUnitRepository orderAirHandlingUnitRepository, OrderFanRepository orderFanRepository, OrderMiningCoolingRackRepository orderMiningCoolingRackRepository, OrderMiningFarmRepository orderMiningFarmRepository, OrdersActionHistoryRepository ordersActionHistoryRepository) {
        this.airHandlingUnitRepository = airHandlingUnitRepository;
        this.airConditioningDeviceRepository = airConditioningDeviceRepository;
        this.fanRepository = fanRepository;
        this.miningCoolingRackRepository = miningCoolingRackRepository;
        this.miningFarmRepository = miningFarmRepository;
        this.userRepository = userRepository;
        this.pageUtils = pageUtils;
        this.miningFarmService = miningFarmService;
        this.miningCoolingRackService = miningCoolingRackService;
        this.airConditioningDeviceService = airConditioningDeviceService;
        this.airHandlingUnitService = airHandlingUnitService;
        this.fanService = fanService;
        this.orderRepository = orderRepository;
        this.orderAirConditioningDeviceRepository = orderAirConditioningDeviceRepository;
        this.orderAirHandlingUnitRepository = orderAirHandlingUnitRepository;
        this.orderFanRepository = orderFanRepository;
        this.orderMiningCoolingRackRepository = orderMiningCoolingRackRepository;
        this.orderMiningFarmRepository = orderMiningFarmRepository;
        this.ordersActionHistoryRepository = ordersActionHistoryRepository;
    }
}
