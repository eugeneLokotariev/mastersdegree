package com.mastersessay.blockchain.accounting.service;

import com.mastersessay.blockchain.accounting.consts.DeviceType;
import com.mastersessay.blockchain.accounting.consts.OrderDevicePurpose;
import com.mastersessay.blockchain.accounting.consts.OrderStatus;
import com.mastersessay.blockchain.accounting.consts.OrderType;
import com.mastersessay.blockchain.accounting.dto.request.order.OrderActionHistoryDto;
import com.mastersessay.blockchain.accounting.dto.request.order.OrderProcessingRequest;
import com.mastersessay.blockchain.accounting.dto.request.order.OrderRequest;
import com.mastersessay.blockchain.accounting.dto.response.facility.OnPremiseDevicesResponse;
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
import java.util.Collections;
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
    public List<OrderResponse> getAllByStatusAndType(String orderType,
                                                     String status) {

        return orderRepository
                .getOrdersByOrderTypeAndStatus(
                        StringUtils.isBlank(orderType) ? null : OrderType.fromTextName(orderType),
                        StringUtils.isBlank(status) ? null : OrderStatus.fromTextName(status))
                .stream()
                .map(this::formOrderResponseFromEntity)
                .collect(Collectors.toList());
    }

    @Transactional
    public List<OrderResponse> getAllByMatchingName(String name) {
        return orderRepository
                .getByMatchingName(name.toUpperCase())
                .stream()
                .map(this::formOrderResponseFromEntity)
                .collect(Collectors.toList());
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
        List<OrderFan> orderFanDs = new ArrayList<>();
        List<OrderAirConditioningDevice> orderAirConditioningDevices = new ArrayList<>();
        List<OrderAirHandlingUnit> orderAirHandlingUnits = new ArrayList<>();
        List<OrdersActionHistory> ordersActionHistories = new ArrayList<>();

        Order savedOrder = orderRepository.save(order);

        if (OrderType.REPLACING.equals(orderRequest.getOrderType())) {
            orderRequest
                    .getOrderMiningFarms()
                    .stream()
                    .map(device -> orderMiningFarmRepository
                            .findById(device.getMiningFarmId()))
                    .forEach(device -> device.ifPresent(item -> {
                                item.setIsOrderCompleted(false);
                                item.setPreviousOrderDevicePurpose(item.getOrderDevicePurpose());
                                item.setOrderDevicePurpose(OrderDevicePurpose.REPLACING);
                                item.setOrder(savedOrder);
                                orderMiningFarms.add(item);
                            })
                    );

            orderRequest
                    .getOrderMiningCoolingRacks()
                    .stream()
                    .map(device -> orderMiningCoolingRackRepository
                            .findById(device.getMiningCoolingRackId()))
                    .forEach(device -> device.ifPresent(item -> {
                                item.setIsOrderCompleted(false);
                                item.setPreviousOrderDevicePurpose(item.getOrderDevicePurpose());
                                item.setOrderDevicePurpose(OrderDevicePurpose.REPLACING);
                                item.setOrder(savedOrder);
                                orderMiningCoolingRacks.add(item);
                            })
                    );

            orderRequest
                    .getOrderFanDs()
                    .stream()
                    .map(device -> orderFanRepository
                            .findById(device.getFanId()))
                    .forEach(device -> device.ifPresent(item -> {
                                item.setIsOrderCompleted(false);
                                item.setPreviousOrderDevicePurpose(item.getOrderDevicePurpose());
                                item.setOrderDevicePurpose(OrderDevicePurpose.REPLACING);
                                item.setOrder(savedOrder);
                                orderFanDs.add(item);
                            })
                    );

            orderRequest
                    .getOrderAirConditioningDevices()
                    .stream()
                    .map(device -> orderAirConditioningDeviceRepository
                            .findById(device.getAirConditioningDeviceId()))
                    .forEach(device -> device.ifPresent(item -> {
                                item.setIsOrderCompleted(false);
                                item.setPreviousOrderDevicePurpose(item.getOrderDevicePurpose());
                                item.setOrderDevicePurpose(OrderDevicePurpose.REPLACING);
                                item.setOrder(savedOrder);
                                orderAirConditioningDevices.add(item);
                            })
                    );

            orderRequest
                    .getOrderAirHandlingUnits()
                    .stream()
                    .map(device -> orderAirHandlingUnitRepository
                            .findById(device.getAirHandlingUnitId()))
                    .forEach(device -> device.ifPresent(
                            item -> {
                                item.setIsOrderCompleted(false);
                                item.setPreviousOrderDevicePurpose(item.getOrderDevicePurpose());
                                item.setOrderDevicePurpose(OrderDevicePurpose.REPLACING);
                                item.setOrder(savedOrder);
                                orderAirHandlingUnits.add(item);
                            })
                    );

            ordersActionHistories.add(OrdersActionHistory
                    .builder()
                    .order(savedOrder)
                    .statusFrom(null)
                    .statusTo(OrderStatus.PLANNED)
                    .actionExecutingDate(LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy hh:mm")))
                    .actionExecutionUsername(userDetails.getUsername())
                    .actionComment(orderRequest.getActionComment())
                    .build());
        } else {
            orderRequest
                    .getOrderMiningFarms()
                    .forEach(miningFarm -> orderMiningFarms.add(OrderMiningFarm
                            .builder()
                            .order(savedOrder)
                            .amount(miningFarm.getAmount())
                            .miningFarm(miningFarmRepository
                                    .getById(miningFarm.getMiningFarmId())
                                    .get())
                            .orderDevicePurpose(miningFarm.getOrderDevicePurpose())
                            .isOrderCompleted(false)
                            .build()));

            orderRequest
                    .getOrderMiningCoolingRacks()
                    .forEach(miningCoolingRack -> orderMiningCoolingRacks.add(OrderMiningCoolingRack
                            .builder()
                            .order(savedOrder)
                            .amount(miningCoolingRack.getAmount())
                            .miningCoolingRack(miningCoolingRackRepository
                                    .getById(miningCoolingRack.getMiningCoolingRackId())
                                    .get())
                            .orderDevicePurpose(miningCoolingRack.getOrderDevicePurpose())
                            .isOrderCompleted(false)
                            .build()));

            orderRequest
                    .getOrderFanDs()
                    .forEach(fan -> orderFanDs.add(OrderFan
                            .builder()
                            .order(savedOrder)
                            .amount(fan.getAmount())
                            .fan(fanRepository
                                    .getById(fan.getFanId())
                                    .get())
                            .orderDevicePurpose(fan.getOrderDevicePurpose())
                            .isOrderCompleted(false)
                            .build()));

            orderRequest
                    .getOrderAirConditioningDevices()
                    .forEach(airConditioningDevice -> orderAirConditioningDevices.add(OrderAirConditioningDevice
                            .builder()
                            .order(savedOrder)
                            .amount(airConditioningDevice.getAmount())
                            .airConditioningDevice(airConditioningDeviceRepository
                                    .getById(airConditioningDevice.getAirConditioningDeviceId())
                                    .get())
                            .orderDevicePurpose(airConditioningDevice.getOrderDevicePurpose())
                            .isOrderCompleted(false)
                            .build()));

            orderRequest
                    .getOrderAirHandlingUnits()
                    .forEach(airHandlingUnit -> orderAirHandlingUnits.add(OrderAirHandlingUnit
                            .builder()
                            .order(savedOrder)
                            .amount(airHandlingUnit.getAmount())
                            .airHandlingUnit(airHandlingUnitRepository
                                    .getById(airHandlingUnit.getAirHandlingUnitId())
                                    .get())
                            .orderDevicePurpose(airHandlingUnit.getOrderDevicePurpose())
                            .isOrderCompleted(false)
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
        }

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
            foundOrder.setWaitingActionUsername(orderRequest.getWaitingActionUsername());
            foundOrder.setModifiedBy(userDetails.getUsername());
            foundOrder.setModifiedWhen(LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy hh:mm")));

            List<OrderMiningFarm> orderMiningFarms = foundOrder.getOrderMiningFarms();
            List<OrderMiningCoolingRack> orderMiningCoolingRacks = foundOrder.getOrderMiningCoolingRacks();
            List<OrderAirConditioningDevice> orderAirConditioningDevices = foundOrder.getOrderAirConditioningDevices();
            List<OrderAirHandlingUnit> orderAirHandlingUnits = foundOrder.getOrderAirHandlingUnits();
            List<OrderFan> orderFanDs = foundOrder.getOrderFans();

            List<OrdersActionHistory> ordersActionHistoryItems = foundOrder.getOrdersActionHistoryItems();

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

            ordersActionHistoryItems.add(OrdersActionHistory
                    .builder()
                    .statusFrom(foundOrder.getStatus())
                    .statusTo(foundOrder.getStatus())
                    .actionComment(ORDER_DEVICES_UPDATE + "'" + userDetails.getUsername() + "'")
                    .actionExecutingDate(LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy hh:mm")))
                    .order(foundOrder)
                    .actionExecutionUsername(userDetails.getUsername())
                    .build());

            Order namedOrder = orderRepository.save(savedUpdatedOrder);

            return formOrderResponseFromEntity(namedOrder);
        }
    }

    @Transactional
    public OrderResponse updateOrderStatus(Long orderId,
                                           OrderProcessingRequest orderProcessingRequest,
                                           UserDetails userDetails) {
        User orderAssignedUser;
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

        if (OrderStatus.CANCELLED.equals(orderProcessingRequest.getStatusTo())
                || OrderStatus.COMPLETED.equals(orderProcessingRequest.getStatusTo())) {
            orderAssignedUser = userRepository
                    .findByUsername(orderProcessingRequest.getWaitingActionUsername())
                    .orElse(null);
        } else {
            orderAssignedUser = userRepository
                    .findByUsername(orderProcessingRequest.getWaitingActionUsername())
                    .orElseThrow(() -> new EntityNotFoundException(USER_NOT_FOUND_WITH_USERNAME_ERROR_MESSAGE));
        }

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

        if (OrderStatus.COMPLETED.equals(orderProcessingRequest.getStatusTo())) {
            foundOrder.getOrderMiningFarms().forEach(farm -> farm.setIsOrderCompleted(true));
            foundOrder.getOrderMiningCoolingRacks().forEach(rack -> rack.setIsOrderCompleted(true));
            foundOrder.getOrderFans().forEach(fan -> fan.setIsOrderCompleted(true));
            foundOrder.getOrderAirConditioningDevices().forEach(device -> device.setIsOrderCompleted(true));
            foundOrder.getOrderAirHandlingUnits().forEach(unit -> unit.setIsOrderCompleted(true));
        }

        foundOrder.setStatus(orderProcessingRequest.getStatusTo());
        foundOrder.setWaitingActionUsername(orderAssignedUser == null ? null : orderAssignedUser.getUsername());
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
                                .previousOrderDevicePurpose(orderMiningFarm.getPreviousOrderDevicePurpose())
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
                                .previousOrderDevicePurpose(orderMiningCoolingRack.getPreviousOrderDevicePurpose())
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
                                .previousOrderDevicePurpose(airConditioningDevice.getPreviousOrderDevicePurpose())
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
                                .previousOrderDevicePurpose(airHandlingUnit.getPreviousOrderDevicePurpose())
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
                                .previousOrderDevicePurpose(orderFan.getPreviousOrderDevicePurpose())
                                .build()
                        )
                        .collect(Collectors.toList())
                )
                .build();
    }

    public OnPremiseDevicesResponse getOnPremiseDevicesByDevicePurpose(String deviceType,
                                                                       String devicePurpose,
                                                                       Integer start,
                                                                       Integer count,
                                                                       String sortBy,
                                                                       String sortType) {
        DeviceType deviceTypeEnum = DeviceType.fromTextName(deviceType);
        OrderDevicePurpose orderDevicePurpose = OrderDevicePurpose.fromTextName(devicePurpose);
        PageRequest pageRequest = pageUtils.formPageRequest(start, count, sortBy, sortType);

        List<OrderFanResponse> onPremiseFans = Collections.emptyList();
        List<OrderMiningFarmResponse> onPremiseMiningFarms = Collections.emptyList();
        List<OrderAirHandlingUnitResponse> onPremiseAirHandlingUnits = Collections.emptyList();
        List<OrderMiningCoolingRackResponse> onPremiseMiningCoolingRacks = Collections.emptyList();
        List<OrderAirConditioningDeviceResponse> onPremiseAirConditioningDevices = Collections.emptyList();

        OnPremiseDevicesResponse.OnPremiseDevicesResponseBuilder builder = OnPremiseDevicesResponse.builder();

        switch (deviceTypeEnum) {
            case FAN:
                Page<OrderFan> pageResultFans = orderFanRepository.findAll(pageRequest);

                if (pageResultFans.hasContent()) {
                    onPremiseFans = pageResultFans
                            .getContent()
                            .stream()
                            .filter(device -> device.getOrderDevicePurpose().equals(orderDevicePurpose)
                                    && device.getIsOrderCompleted())
                            .map(orderFan -> OrderFanResponse
                                    .builder()
                                    .amount(orderFan.getAmount())
                                    .fan(fanService.getById(orderFan.getFan().getId()))
                                    .orderDevicePurpose(orderFan.getOrderDevicePurpose())
                                    .build()
                            )
                            .collect(Collectors.toList());
                }

                break;
            case MINING_FARM:
                Page<OrderMiningFarm> pageResultFarms = orderMiningFarmRepository.findAll(pageRequest);

                if (pageResultFarms.hasContent()) {
                    onPremiseMiningFarms = pageResultFarms
                            .getContent()
                            .stream()
                            .filter(device -> device.getOrderDevicePurpose().equals(orderDevicePurpose)
                                    && device.getIsOrderCompleted())
                            .map(orderMiningFarm -> OrderMiningFarmResponse
                                    .builder()
                                    .amount(orderMiningFarm.getAmount())
                                    .miningFarm(miningFarmService.getById(orderMiningFarm.getMiningFarm().getId()))
                                    .orderDevicePurpose(orderMiningFarm.getOrderDevicePurpose())
                                    .build()
                            )
                            .collect(Collectors.toList());
                }

                break;
            case AIR_HANDLING_UNIT:
                Page<OrderAirHandlingUnit> pageResultAirHandlingUnits = orderAirHandlingUnitRepository.findAll(pageRequest);

                if (pageResultAirHandlingUnits.hasContent()) {
                    onPremiseAirHandlingUnits = pageResultAirHandlingUnits
                            .getContent()
                            .stream()
                            .filter(device -> device.getOrderDevicePurpose().equals(orderDevicePurpose)
                                    && device.getIsOrderCompleted())
                            .map(airHandlingUnit -> OrderAirHandlingUnitResponse
                                    .builder()
                                    .amount(airHandlingUnit.getAmount())
                                    .airHandlingUnit(airHandlingUnitService.getById(airHandlingUnit.getAirHandlingUnit().getId()))
                                    .orderDevicePurpose(airHandlingUnit.getOrderDevicePurpose())
                                    .build()
                            )
                            .collect(Collectors.toList());
                }

                break;
            case MINING_COOLING_RACK:
                Page<OrderMiningCoolingRack> pageResultMiningCoolingRacks = orderMiningCoolingRackRepository.findAll(pageRequest);

                if (pageResultMiningCoolingRacks.hasContent()) {
                    onPremiseMiningCoolingRacks = pageResultMiningCoolingRacks
                            .getContent()
                            .stream()
                            .filter(device -> device.getOrderDevicePurpose().equals(orderDevicePurpose)
                                    && device.getIsOrderCompleted())
                            .map(orderMiningCoolingRack -> OrderMiningCoolingRackResponse
                                    .builder()
                                    .amount(orderMiningCoolingRack.getAmount())
                                    .miningCooling(miningCoolingRackService.getById(orderMiningCoolingRack.getMiningCoolingRack().getId()))
                                    .orderDevicePurpose(orderMiningCoolingRack.getOrderDevicePurpose())
                                    .build()
                            )
                            .collect(Collectors.toList());
                }

                break;
            case AIR_CONDITIONING_DEVICE:
                Page<OrderAirConditioningDevice> pageAirConditioningDevices = orderAirConditioningDeviceRepository.findAll(pageRequest);

                if (pageAirConditioningDevices.hasContent()) {
                    onPremiseAirConditioningDevices = pageAirConditioningDevices
                            .getContent()
                            .stream()
                            .filter(device -> device.getOrderDevicePurpose().equals(orderDevicePurpose)
                                    && device.getIsOrderCompleted())
                            .map(airConditioningDevice -> OrderAirConditioningDeviceResponse
                                    .builder()
                                    .amount(airConditioningDevice.getAmount())
                                    .airConditioningDevice(airConditioningDeviceService.getById(airConditioningDevice.getAirConditioningDevice().getId()))
                                    .orderDevicePurpose(airConditioningDevice.getOrderDevicePurpose())
                                    .build()
                            )
                            .collect(Collectors.toList());
                }

                break;
        }

        return builder
                .onPremiseFans(onPremiseFans)
                .onPremiseMiningFarms(onPremiseMiningFarms)
                .onPremiseAirHandlingUnits(onPremiseAirHandlingUnits)
                .onPremiseMiningCoolingRacks(onPremiseMiningCoolingRacks)
                .onPremiseAirConditioningDevices(onPremiseAirConditioningDevices)
                .build();
    }

    @Autowired
    public OrderService(AirHandlingUnitRepository airHandlingUnitRepository,
                        AirConditioningDeviceRepository airConditioningDeviceRepository,
                        FanRepository fanRepository,
                        MiningCoolingRackRepository miningCoolingRackRepository,
                        MiningFarmRepository miningFarmRepository,
                        UserRepository userRepository,
                        PageUtils pageUtils,
                        MiningFarmService miningFarmService,
                        MiningCoolingRackService miningCoolingRackService,
                        AirConditioningDeviceService airConditioningDeviceService,
                        AirHandlingUnitService airHandlingUnitService,
                        FanService fanService,
                        OrderRepository orderRepository,
                        OrderAirConditioningDeviceRepository orderAirConditioningDeviceRepository,
                        OrderAirHandlingUnitRepository orderAirHandlingUnitRepository,
                        OrderFanRepository orderFanRepository,
                        OrderMiningCoolingRackRepository orderMiningCoolingRackRepository,
                        OrderMiningFarmRepository orderMiningFarmRepository,
                        OrdersActionHistoryRepository ordersActionHistoryRepository) {
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
