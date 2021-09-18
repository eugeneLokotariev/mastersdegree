package com.mastersessay.blockchain.accounting.controller;

import com.mastersessay.blockchain.accounting.dto.request.facility.MiningFarmRequest;
import com.mastersessay.blockchain.accounting.dto.response.facility.MiningCoolingResponse;
import com.mastersessay.blockchain.accounting.dto.response.facility.MiningFarmResponse;
import com.mastersessay.blockchain.accounting.service.MiningFarmService;
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

import javax.validation.Valid;
import java.util.List;

import static com.mastersessay.blockchain.accounting.consts.BlockchainAccountingConstants.Http.REQUEST_PROCESSING_END_MESSAGE;
import static com.mastersessay.blockchain.accounting.consts.BlockchainAccountingConstants.Http.REQUEST_PROCESSING_START_MESSAGE;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/v1/miningFarms")
@SecurityRequirement(name = "bearerAuth")
@SuppressWarnings("Duplicates")
public class MiningFarmController {
    private static final Logger log = LoggerFactory.getLogger(ManufacturerController.class);

    private final MiningFarmService miningFarmService;

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('CATALOG_ADMIN')")
    public ResponseEntity<?> getById(@PathVariable("id") Long id) {
        log.info(REQUEST_PROCESSING_START_MESSAGE);
        log.info("GET /api/v1/miningFarm/{}", id);

        MiningFarmResponse miningFarmById = miningFarmService.getById(id);

        log.info("Response code = {}", HttpStatus.OK);
        log.info(REQUEST_PROCESSING_END_MESSAGE);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(miningFarmById);
    }

    @GetMapping("/search/{name}")
    @PreAuthorize("hasRole('CATALOG_ADMIN')")
    public ResponseEntity<?> getByMatchingName(@PathVariable("name") String name) {
        log.info(REQUEST_PROCESSING_START_MESSAGE);
        log.info("GET /search/{}", name);

        List<MiningFarmResponse> resultList = miningFarmService.getByMatchingName(name);

        log.info("Response code = {}", HttpStatus.OK);
        log.info(REQUEST_PROCESSING_END_MESSAGE);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(resultList);
    }

    @GetMapping
    @PreAuthorize("hasRole('CATALOG_ADMIN')")
    public ResponseEntity<?> getByAll(@RequestParam(value = "start", required = false, defaultValue = "0") Integer start,
                                      @RequestParam(value = "count", required = false, defaultValue = "5") Integer count,
                                      @RequestParam(value = "sortBy", required = false, defaultValue = "model") String sortBy,
                                      @RequestParam(value = "sortType", required = false, defaultValue = "asc") String sortType) {
        log.info(REQUEST_PROCESSING_START_MESSAGE);
        log.info("GET /api/v1/miningFarm?start={}&count={}&sortBy={}&sortType={}",
                start,
                count,
                sortBy,
                sortType);

        List<MiningFarmResponse> miningFarms =
                miningFarmService.getAll(start, count, sortBy, sortType);

        log.info("Response code = {}", HttpStatus.OK);
        log.info(REQUEST_PROCESSING_END_MESSAGE);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(miningFarms);
    }

    @PostMapping
    @PreAuthorize("hasRole('CATALOG_ADMIN')")
    public ResponseEntity<?> create(@RequestBody @Valid MiningFarmRequest miningFarmRequest) {
        log.info(REQUEST_PROCESSING_START_MESSAGE);
        log.info("POST /api/v1/miningFarm. Body = {}", miningFarmRequest.toString());

        UserDetails principal = (UserDetails) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();
        MiningFarmResponse createdMiningFarm = miningFarmService.create(miningFarmRequest, principal);

        log.info("Response code = {}", HttpStatus.OK);
        log.info(REQUEST_PROCESSING_END_MESSAGE);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(createdMiningFarm);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('CATALOG_ADMIN')")
    public ResponseEntity<?> update(@PathVariable("id") Long id,
                                    @RequestBody @Valid MiningFarmRequest miningFarmRequest) {
        log.info(REQUEST_PROCESSING_START_MESSAGE);
        log.info("PUT /api/v1/miningFarm/{}. Body = {}", id, miningFarmRequest.toString());

        UserDetails principal = (UserDetails) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();
        MiningFarmResponse updatedMiningFarm = miningFarmService.update(id, miningFarmRequest, principal);

        log.info("Response code = {}", HttpStatus.OK);
        log.info(REQUEST_PROCESSING_END_MESSAGE);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(updatedMiningFarm);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('CATALOG_ADMIN')")
    public ResponseEntity<?> delete(@PathVariable("id") Long id) {
        log.info(REQUEST_PROCESSING_START_MESSAGE);
        log.info("DELETE /api/v1/miningFarm/{}", id);

        miningFarmService.deleteById(id);

        log.info("Response code = {}", HttpStatus.OK);
        log.info(REQUEST_PROCESSING_END_MESSAGE);

        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .build();
    }

    @Autowired
    public MiningFarmController(MiningFarmService miningFarmService) {
        this.miningFarmService = miningFarmService;
    }
}
