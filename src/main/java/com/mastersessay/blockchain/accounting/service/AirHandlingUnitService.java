package com.mastersessay.blockchain.accounting.service;

import com.mastersessay.blockchain.accounting.dto.request.facility.AirHandlingUnitRequest;
import com.mastersessay.blockchain.accounting.dto.request.facility.FanRequest;
import com.mastersessay.blockchain.accounting.dto.response.facility.AirHandlingUnitResponse;
import com.mastersessay.blockchain.accounting.dto.response.facility.FanResponse;
import com.mastersessay.blockchain.accounting.dto.response.facility.MiningFarmResponse;
import com.mastersessay.blockchain.accounting.model.dictionary.Manufacturer;
import com.mastersessay.blockchain.accounting.model.dictionary.facility.AirHandlingUnit;
import com.mastersessay.blockchain.accounting.model.dictionary.facility.Fan;
import com.mastersessay.blockchain.accounting.repository.AirHandlingUnitRepository;
import com.mastersessay.blockchain.accounting.repository.FanRepository;
import com.mastersessay.blockchain.accounting.repository.ManufacturerRepository;
import com.mastersessay.blockchain.accounting.repository.UserRepository;
import com.mastersessay.blockchain.accounting.util.PageUtils;
import com.mastersessay.blockchain.accounting.util.converters.AirHandlingUtils;
import com.mastersessay.blockchain.accounting.util.converters.FanUtils;
import com.mastersessay.blockchain.accounting.util.converters.ManufacturerUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.mastersessay.blockchain.accounting.consts.BlockchainAccountingConstants.BusinessMessages.*;

@SuppressWarnings("Duplicates")
@Service
public class AirHandlingUnitService {
    private static final Logger log = LoggerFactory.getLogger(FanService.class);

    private final AirHandlingUnitRepository airHandlingUnitRepository;
    private final ManufacturerService manufacturerService;
    private final AirHandlingUtils airHandlingUtils;

    private final GenericCrudProcessor<Long, AirHandlingUnit, AirHandlingUnitRequest, AirHandlingUnitResponse> airHandlingCrudProcessor;

    @Transactional
    public AirHandlingUnitResponse getById(Long id) {
        return airHandlingCrudProcessor.getById(
                id,
                airHandlingUnitRepository,
                airHandlingUtils
        );
    }

    @Transactional
    public List<AirHandlingUnitResponse> getByMatchingName(String name) {
        return airHandlingCrudProcessor.getAllByMatchingName(name, airHandlingUnitRepository, airHandlingUtils);
    }

    @Transactional
    public List<AirHandlingUnitResponse> getAll(Integer start,
                                                Integer count,
                                                String sortBy,
                                                String sortType) {
        return airHandlingCrudProcessor.getAll(
                start,
                count,
                sortBy,
                sortType,
                airHandlingUnitRepository,
                airHandlingUtils
        );
    }

    @Transactional
    public AirHandlingUnitResponse create(AirHandlingUnitRequest airHandlingUnitRequest,
                                          UserDetails user) {
        Optional<AirHandlingUnit> existingUnit = airHandlingUnitRepository.getByModel(airHandlingUnitRequest.getModel());

        existingUnit.ifPresent(fan -> {
            if (fan.getManufacturer().getName()
                    .equals(airHandlingUnitRequest.getManufacturer().getName())) {
                throw new IllegalArgumentException(OBJECT_EXISTS_MESSAGE);
            }
        });

        return airHandlingCrudProcessor.create(
                airHandlingUnitRequest,
                user,
                manufacturerService.findOrCreateByName(airHandlingUnitRequest.getManufacturer(), user),
                airHandlingUnitRepository,
                airHandlingUtils
        );
    }

    @Transactional
    public AirHandlingUnitResponse update(Long id,
                                          AirHandlingUnitRequest airHandlingUnitRequest,
                                          UserDetails user) {
        return airHandlingCrudProcessor.update(
                id,
                airHandlingUnitRequest,
                manufacturerService.getByName(airHandlingUnitRequest.getManufacturer().getName()),
                user,
                airHandlingUnitRepository,
                airHandlingUtils
        );
    }

    @Transactional
    public void deleteById(Long id) {
        airHandlingCrudProcessor.deleteById(id, airHandlingUnitRepository);
    }

    @Autowired
    public AirHandlingUnitService(AirHandlingUnitRepository airHandlingUnitRepository,
                                  ManufacturerService manufacturerService,
                                  AirHandlingUtils airHandlingUtils,
                                  GenericCrudProcessor<Long, AirHandlingUnit, AirHandlingUnitRequest, AirHandlingUnitResponse> airHandlingCrudProcessor) {
        this.airHandlingUnitRepository = airHandlingUnitRepository;
        this.manufacturerService = manufacturerService;
        this.airHandlingUtils = airHandlingUtils;
        this.airHandlingCrudProcessor = airHandlingCrudProcessor;
    }
}
