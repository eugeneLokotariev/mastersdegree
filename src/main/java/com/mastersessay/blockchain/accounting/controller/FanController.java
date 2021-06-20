package com.mastersessay.blockchain.accounting.controller;

import com.mastersessay.blockchain.accounting.dto.request.facility.FanRequest;
import com.mastersessay.blockchain.accounting.dto.response.facility.FanResponse;
import com.mastersessay.blockchain.accounting.service.FanService;
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
@RequestMapping("/api/v1/fans")
@SecurityRequirement(name = "bearerAuth")
@SuppressWarnings("Duplicates")
public class FanController {
    private static final Logger log = LoggerFactory.getLogger(FanController.class);

    private final FanService fanService;

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('CATALOG_ADMIN')")
    public ResponseEntity<?> getManufacturerByName(@PathVariable("id") Long id) {
        log.info(REQUEST_PROCESSING_START_MESSAGE);
        log.info("GET /api/v1/fans/{}", id);

        FanResponse byId = fanService.getById(id);

        log.info("Response code = {}", HttpStatus.OK);
        log.info(REQUEST_PROCESSING_END_MESSAGE);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(byId);
    }

    @GetMapping
    @PreAuthorize("hasRole('CATALOG_ADMIN')")
    public ResponseEntity<?> findAll(@RequestParam(value = "start", required = false, defaultValue = "0") Integer start,
                                     @RequestParam(value = "count", required = false, defaultValue = "5") Integer count,
                                     @RequestParam(value = "sortBy", required = false, defaultValue = "orderId") String sortBy,
                                     @RequestParam(value = "sortType", required = false, defaultValue = "asc") String sortType) {
        log.info(REQUEST_PROCESSING_START_MESSAGE);
        log.info("GET /api/v1/fans?start={}&count={}&sortBy={}&sortType={}",
                start,
                count,
                sortBy,
                sortType);

        List<FanResponse> all = fanService
                .getAll(start,
                        count,
                        sortBy,
                        sortType);

        log.info("Response code = {}", HttpStatus.OK);
        log.info(REQUEST_PROCESSING_END_MESSAGE);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(all);
    }

    @PostMapping
    @PreAuthorize("hasRole('CATALOG_ADMIN')")
    public ResponseEntity<?> create(@RequestBody FanRequest fanRequest) {
        log.info(REQUEST_PROCESSING_START_MESSAGE);
        log.info("POST /api/v1/fans. body = {}", fanRequest);

        UserDetails principal = (UserDetails) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();
        FanResponse fanResponse = fanService.create(fanRequest, principal);

        log.info("Response code = {}", HttpStatus.CREATED);
        log.info(REQUEST_PROCESSING_END_MESSAGE);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(fanResponse);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('CATALOG_ADMIN')")
    public ResponseEntity<?> update(@PathVariable("id") Long id, @RequestBody FanRequest fanRequest) {
        log.info(REQUEST_PROCESSING_START_MESSAGE);
        log.info("PUT /api/v1/fans/{}", id);

        UserDetails principal = (UserDetails) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();
        FanResponse updated = fanService.update(id, fanRequest, principal);

        log.info("Response code = {}", HttpStatus.NO_CONTENT);
        log.info(REQUEST_PROCESSING_END_MESSAGE);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(updated);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('CATALOG_ADMIN')")
    public ResponseEntity<?> delete(@PathVariable("id") Long id) {
        log.info(REQUEST_PROCESSING_START_MESSAGE);
        log.info("DELETE /api/v1/fans/{}", id);

        fanService.deleteById(id);

        log.info("Response code = {}", HttpStatus.NO_CONTENT);
        log.info(REQUEST_PROCESSING_END_MESSAGE);

        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .build();
    }

    @Autowired
    public FanController(FanService fanService) {
        this.fanService = fanService;
    }
}
