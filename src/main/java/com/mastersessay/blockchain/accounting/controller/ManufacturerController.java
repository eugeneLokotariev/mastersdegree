package com.mastersessay.blockchain.accounting.controller;

import com.mastersessay.blockchain.accounting.consts.DeviceType;
import com.mastersessay.blockchain.accounting.dto.request.facility.ManufacturerRequest;
import com.mastersessay.blockchain.accounting.dto.response.facility.ManufacturerResponse;
import com.mastersessay.blockchain.accounting.service.ManufacturerService;
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
@RequestMapping("/api/v1/manufacturers")
@SecurityRequirement(name = "bearerAuth")
@SuppressWarnings("Duplicates")
public class ManufacturerController {
    private static final Logger log = LoggerFactory.getLogger(ManufacturerController.class);

    private final ManufacturerService manufacturerService;

    @GetMapping("/{manufacturerName}")
    @PreAuthorize("hasRole('CATALOG_ADMIN')")
    public ResponseEntity<?> getManufacturerByName(@PathVariable("manufacturerName") String manufacturerName) {
        log.info(REQUEST_PROCESSING_START_MESSAGE);
        log.info("GET /api/v1/manufacturer/{}", manufacturerName);

        ManufacturerResponse manufacturerByName = manufacturerService.getByNameAsResponse(manufacturerName);

        log.info("Response code = {}", HttpStatus.OK);
        log.info(REQUEST_PROCESSING_END_MESSAGE);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(manufacturerByName);
    }

    @GetMapping
    @PreAuthorize("hasRole('CATALOG_ADMIN')")
    public ResponseEntity<?> findAll(@RequestParam(value = "start", required = false, defaultValue = "0") Integer start,
                                     @RequestParam(value = "count", required = false, defaultValue = "5") Integer count,
                                     @RequestParam(value = "sortBy", required = false, defaultValue = "orderId") String sortBy,
                                     @RequestParam(value = "sortType", required = false, defaultValue = "asc") String sortType) {
        log.info(REQUEST_PROCESSING_START_MESSAGE);
        log.info("GET /api/v1/manufacturer?start={}&count={}&sortBy={}&sortType={}",
                start,
                count,
                sortBy,
                sortType);

        List<ManufacturerResponse> all = manufacturerService
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
    public ResponseEntity<?> create(@RequestBody ManufacturerRequest manufacturerRequest) {
        log.info(REQUEST_PROCESSING_START_MESSAGE);
        log.info("POST /api/v1/manufacturer. body = {}", manufacturerRequest);

        UserDetails principal = (UserDetails) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();
        ManufacturerResponse manufacturerResponse = manufacturerService.create(manufacturerRequest, principal);

        log.info("Response code = {}", HttpStatus.CREATED);
        log.info(REQUEST_PROCESSING_END_MESSAGE);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(manufacturerResponse);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('CATALOG_ADMIN')")
    public ResponseEntity<?> update(@PathVariable("id") Long id, @RequestBody ManufacturerRequest manufacturerRequest) {
        log.info(REQUEST_PROCESSING_START_MESSAGE);
        log.info("PUT /api/v1/manufacturer/{}", id);

        UserDetails principal = (UserDetails) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();
        ManufacturerResponse updated = manufacturerService.update(id, manufacturerRequest, principal);

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
        log.info("DELETE /api/v1/manufacturer/{}", id);

        manufacturerService.delete(id);

        log.info("Response code = {}", HttpStatus.NO_CONTENT);
        log.info(REQUEST_PROCESSING_END_MESSAGE);

        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .build();
    }

    @Autowired
    public ManufacturerController(ManufacturerService manufacturerService) {
        this.manufacturerService = manufacturerService;
    }
}
