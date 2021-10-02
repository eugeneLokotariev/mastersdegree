package com.mastersessay.blockchain.accounting.controller;

import com.mastersessay.blockchain.accounting.dto.request.facility.MiningCoolingRackRequest;
import com.mastersessay.blockchain.accounting.dto.response.facility.AirHandlingUnitResponse;
import com.mastersessay.blockchain.accounting.dto.response.facility.MiningCoolingResponse;
import com.mastersessay.blockchain.accounting.service.MiningCoolingRackService;
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
@RequestMapping("/api/v1/miningCoolingRacks")
@SecurityRequirement(name = "bearerAuth")
@SuppressWarnings("Duplicates")
public class MiningCoolingRackController {
    private static final Logger log = LoggerFactory.getLogger(ManufacturerController.class);

    private final MiningCoolingRackService miningCoolingRackService;

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('CATALOG_ADMIN') or hasRole('ORDER_ADMIN') or hasRole('MAINTENANCE_ADMIN')")
    public ResponseEntity<?> getDeviceById(@PathVariable("id") Long id) {
        log.info(REQUEST_PROCESSING_START_MESSAGE);
        log.info("GET /api/v1/miningCoolingRack/{}", id);

        MiningCoolingResponse miningCoolingRack = miningCoolingRackService.getById(id);

        log.info("Response code = {}", HttpStatus.OK);
        log.info(REQUEST_PROCESSING_END_MESSAGE);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(miningCoolingRack);
    }

    @GetMapping("/search")
    @PreAuthorize("hasRole('CATALOG_ADMIN') or hasRole('ORDER_ADMIN') or hasRole('MAINTENANCE_ADMIN')")
    public ResponseEntity<?> getByMatchingName(@RequestParam("name") String name) {
        log.info(REQUEST_PROCESSING_START_MESSAGE);
        log.info("GET /search?name={}", name);

        List<MiningCoolingResponse> resultList = miningCoolingRackService.getByMatchingName(name);

        log.info("Response code = {}", HttpStatus.OK);
        log.info(REQUEST_PROCESSING_END_MESSAGE);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(resultList);
    }

    @GetMapping
    @PreAuthorize("hasRole('CATALOG_ADMIN') or hasRole('ORDER_ADMIN') or hasRole('MAINTENANCE_ADMIN')")
    public ResponseEntity<?> findAll(@RequestParam(value = "start", required = false, defaultValue = "0") Integer start,
                                     @RequestParam(value = "count", required = false, defaultValue = "5") Integer count,
                                     @RequestParam(value = "sortBy", required = false, defaultValue = "orderId") String sortBy,
                                     @RequestParam(value = "sortType", required = false, defaultValue = "asc") String sortType) {
        log.info(REQUEST_PROCESSING_START_MESSAGE);
        log.info("GET /api/v1/miningCoolingRack?start={}&count={}&sortBy={}&sortType={}",
                start,
                count,
                sortBy,
                sortType);

        List<MiningCoolingResponse> all = miningCoolingRackService
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
    public ResponseEntity<?> create(@RequestBody MiningCoolingRackRequest miningCoolingRackRequest) {
        log.info(REQUEST_PROCESSING_START_MESSAGE);
        log.info("POST /api/v1/miningCoolingRack. body = {}", miningCoolingRackRequest);

        UserDetails principal = (UserDetails) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();
        MiningCoolingResponse miningCoolingRack = miningCoolingRackService.create(miningCoolingRackRequest, principal);

        log.info("Response code = {}", HttpStatus.CREATED);
        log.info(REQUEST_PROCESSING_END_MESSAGE);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(miningCoolingRack);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('CATALOG_ADMIN')")
    public ResponseEntity<?> update(@PathVariable("id") Long id, @RequestBody MiningCoolingRackRequest miningCoolingRackRequest) {
        log.info(REQUEST_PROCESSING_START_MESSAGE);
        log.info("PUT /api/v1/miningCoolingRack/{}", id);

        UserDetails principal = (UserDetails) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();
        MiningCoolingResponse updated = miningCoolingRackService.update(id, miningCoolingRackRequest, principal);

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

        miningCoolingRackService.deleteById(id);

        log.info("Response code = {}", HttpStatus.NO_CONTENT);
        log.info(REQUEST_PROCESSING_END_MESSAGE);

        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .build();
    }

    @Autowired
    public MiningCoolingRackController(MiningCoolingRackService miningCoolingRackService) {
        this.miningCoolingRackService = miningCoolingRackService;
    }
}
