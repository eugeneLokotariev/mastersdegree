package com.mastersessay.blockchain.accounting.controller;

import com.mastersessay.blockchain.accounting.dto.request.order.OrderProcessingRequest;
import com.mastersessay.blockchain.accounting.dto.request.order.OrderRequest;
import com.mastersessay.blockchain.accounting.dto.response.order.OrderResponse;
import com.mastersessay.blockchain.accounting.service.OrderService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.mastersessay.blockchain.accounting.consts.BlockchainAccountingConstants.Http.REQUEST_PROCESSING_END_MESSAGE;
import static com.mastersessay.blockchain.accounting.consts.BlockchainAccountingConstants.Http.REQUEST_PROCESSING_START_MESSAGE;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/v1/orders")
@SecurityRequirement(name = "bearerAuth")
@SuppressWarnings("Duplicates")
public class OrderController {
    private static final Logger log = LoggerFactory.getLogger(OrderController.class);

    private final OrderService orderService;

    @PreAuthorize("hasRole('ORDER_ADMIN') or hasRole('MAINTENANCE_ADMIN')")
    @GetMapping
    public ResponseEntity<?> getOrders(@RequestParam(value = "start", required = false, defaultValue = "0") Integer start,
                                       @RequestParam(value = "count", required = false, defaultValue = "5") Integer count,
                                       @RequestParam(value = "sortBy", required = false, defaultValue = "orderId") String sortBy,
                                       @RequestParam(value = "sortType", required = false, defaultValue = "asc") String sortType) {
        log.info(REQUEST_PROCESSING_START_MESSAGE);
        log.info("GET /api/v1/orders?start={}&count={}&sortBy={}&sortType={}",
                start,
                count,
                sortBy,
                sortType);

        List<OrderResponse> all = orderService.getAll(start, count, sortBy, sortType);

        log.info("Response code = {}", HttpStatus.OK);
        log.info(REQUEST_PROCESSING_END_MESSAGE);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(all);
    }

    @PreAuthorize("hasRole('ORDER_ADMIN') or hasRole('MAINTENANCE_ADMIN')")
    @GetMapping("/assigned")
    public ResponseEntity<?> getOrdersAssignedOnUser(@RequestParam(value = "username") String username,
                                                     @RequestParam(value = "start", required = false, defaultValue = "0") Integer start,
                                                     @RequestParam(value = "count", required = false, defaultValue = "5") Integer count,
                                                     @RequestParam(value = "sortBy", required = false, defaultValue = "orderId") String sortBy,
                                                     @RequestParam(value = "sortType", required = false, defaultValue = "asc") String sortType) {
        log.info(REQUEST_PROCESSING_START_MESSAGE);
        log.info("GET /api/v1/orders?start={}&count={}&sortBy={}&sortType={}&username={}",
                start,
                count,
                sortBy,
                sortType,
                username);

        List<OrderResponse> allByUsername = orderService.getAllByUsername(username, start, count, sortBy, sortType);

        log.info("Response code = {}", HttpStatus.OK);
        log.info(REQUEST_PROCESSING_END_MESSAGE);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(allByUsername);
    }

    @PreAuthorize("hasRole('ORDER_ADMIN') or hasRole('MAINTENANCE_ADMIN')")
    @PostMapping("/initiate")
    public ResponseEntity<?> initiateOrder(@RequestBody OrderRequest orderRequest) {
        log.info(REQUEST_PROCESSING_START_MESSAGE);
        log.info("POST /api/v1/initiate. body = {}", orderRequest);

        UserDetails principal = (UserDetails) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();

        OrderResponse orderResponse = orderService.initiateOrder(orderRequest, principal);

        log.info("Response code = {}", HttpStatus.OK);
        log.info(REQUEST_PROCESSING_END_MESSAGE);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(orderResponse);
    }

    @PreAuthorize("hasRole('ORDER_ADMIN') or hasRole('MAINTENANCE_ADMIN')")
    @PatchMapping("/{id}")
    public ResponseEntity<?> updateOrder(@PathVariable("id") Long id,
                                         @RequestBody OrderRequest orderRequest) {
        log.info(REQUEST_PROCESSING_START_MESSAGE);
        log.info("PUT /api/v1/orders/{}. body = {}",
                id,
                orderRequest);

        UserDetails principal = (UserDetails) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();

        OrderResponse orderResponse = orderService.updateOrder(id, orderRequest, principal);

        log.info("Response code = {}", HttpStatus.OK);
        log.info(REQUEST_PROCESSING_END_MESSAGE);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(orderResponse);
    }

    @PreAuthorize("hasRole('ORDER_ADMIN') or hasRole('MAINTENANCE_ADMIN')")
    @PatchMapping("/process/{id}")
    public ResponseEntity<?> updateOrderStatus(@PathVariable("id") Long id,
                                               @RequestBody OrderProcessingRequest orderProcessingRequest) {
        log.info(REQUEST_PROCESSING_START_MESSAGE);
        log.info("PATCH /api/v1/processOrder. body = {}", orderProcessingRequest);

        UserDetails principal = (UserDetails) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();

        OrderResponse orderResponse = orderService.updateOrderStatus(id, orderProcessingRequest, principal);

        log.info("Response code = {}", HttpStatus.OK);
        log.info(REQUEST_PROCESSING_END_MESSAGE);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(orderResponse);
    }

    @Autowired
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }
}
