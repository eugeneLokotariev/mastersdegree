package com.mastersessay.blockchain.accounting.controller;

import com.mastersessay.blockchain.accounting.dto.response.facility.OnPremiseDevicesResponse;
import com.mastersessay.blockchain.accounting.service.OrderService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.mastersessay.blockchain.accounting.consts.BlockchainAccountingConstants.Http.REQUEST_PROCESSING_END_MESSAGE;
import static com.mastersessay.blockchain.accounting.consts.BlockchainAccountingConstants.Http.REQUEST_PROCESSING_START_MESSAGE;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/v1/onPremiseDevices")
@SecurityRequirement(name = "bearerAuth")
@SuppressWarnings("Duplicates")
public class OnPremiseDevicesController {
    private static final Logger log = LoggerFactory.getLogger(OrderController.class);

    private final OrderService orderService;

    @GetMapping
    @PreAuthorize("hasRole('ORDER_ADMIN') or hasRole('MAINTENANCE_ADMIN')")
    public ResponseEntity<?> getOnPremiseDevicesByCriteria(@RequestParam("deviceType") List<String> deviceType,
                                                           @RequestParam("devicePurpose") String devicePurpose,
                                                           @RequestParam(value = "start", required = false, defaultValue = "0") Integer start,
                                                           @RequestParam(value = "count", required = false, defaultValue = "5") Integer count,
                                                           @RequestParam(value = "sortBy", required = false, defaultValue = "orderId") String sortBy,
                                                           @RequestParam(value = "sortType", required = false, defaultValue = "asc") String sortType) {
        log.info(REQUEST_PROCESSING_START_MESSAGE);
        log.info("GET /api/v1/onPremiseDevices/devicePurpose={}?deviceType={}", devicePurpose, deviceType);

        OnPremiseDevicesResponse onPremiseDevicesByDevicePurpose = orderService.getOnPremiseDevicesByDevicePurpose(
                deviceType,
                devicePurpose,
                start,
                count,
                sortBy,
                sortType);

        log.info("Response code = {}", HttpStatus.OK);
        log.info(REQUEST_PROCESSING_END_MESSAGE);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(onPremiseDevicesByDevicePurpose);
    }

    @Autowired
    public OnPremiseDevicesController(OrderService orderService) {
        this.orderService = orderService;
    }
}
