package com.mastersessay.blockchain.accounting.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.mastersessay.blockchain.accounting.consts.BlockchainAccountingConstants.Http.REQUEST_PROCESSING_END_MESSAGE;
import static com.mastersessay.blockchain.accounting.consts.BlockchainAccountingConstants.Http.REQUEST_PROCESSING_START_MESSAGE;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/test")
@SecurityRequirement(name = "bearerAuth")
public class TestController {
    private static final Logger log = LoggerFactory.getLogger(TestController.class);

    @GetMapping("/catalogAdmin")
    @PreAuthorize("hasRole('CATALOG_ADMIN')")
    public String catalogAdmin() {
        log.info(REQUEST_PROCESSING_START_MESSAGE);
        log.info("GET /api/test/catalogAdmin");
        log.info("Response code = {}", HttpStatus.OK);
        log.info(REQUEST_PROCESSING_END_MESSAGE);

        return "ROLE_CATALOG_ADMIN API";
    }

    @GetMapping("/orderAdmin")
    @PreAuthorize("hasRole('ORDER_ADMIN')")
    public String orderAdmin() {
        log.info(REQUEST_PROCESSING_START_MESSAGE);
        log.info("GET /api/test/orderAdmin");
        log.info("Response code = {}", HttpStatus.OK);
        log.info(REQUEST_PROCESSING_END_MESSAGE);

        return "ROLE_ORDER_ADMIN API";
    }

    @GetMapping("/maintenanceAdmin")
    @PreAuthorize("hasRole('MAINTENANCE_ADMIN')")
    public String maintenanceAdmin() {
        log.info(REQUEST_PROCESSING_START_MESSAGE);
        log.info("GET /api/test/maintenanceAdmin");
        log.info("Response code = {}", HttpStatus.OK);
        log.info(REQUEST_PROCESSING_END_MESSAGE);

        return "ROLE_MAINTENANCE_ADMIN API";
    }

    @GetMapping("/userAdmin")
    @PreAuthorize("hasRole('USER_ADMIN')")
    public String userAdmin() {
        log.info(REQUEST_PROCESSING_START_MESSAGE);
        log.info("GET /api/test/userAdmin");
        log.info("Response code = {}", HttpStatus.OK);
        log.info(REQUEST_PROCESSING_END_MESSAGE);

        return "ROLE_USER_ADMIN API";
    }

    @GetMapping("/plainUser")
    @PreAuthorize("hasRole('PLAIN_USER')")
    public String plainUser() {
        log.info(REQUEST_PROCESSING_START_MESSAGE);
        log.info("GET /api/test/plainUser");
        log.info("Response code = {}", HttpStatus.OK);
        log.info(REQUEST_PROCESSING_END_MESSAGE);

        return "PLAIN_USER API";
    }
}
