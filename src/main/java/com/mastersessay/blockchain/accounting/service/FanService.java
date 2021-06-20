package com.mastersessay.blockchain.accounting.service;

import com.mastersessay.blockchain.accounting.dto.request.facility.FanRequest;
import com.mastersessay.blockchain.accounting.dto.request.facility.MiningCoolingRackRequest;
import com.mastersessay.blockchain.accounting.dto.response.facility.FanResponse;
import com.mastersessay.blockchain.accounting.dto.response.facility.MiningCoolingResponse;
import com.mastersessay.blockchain.accounting.model.dictionary.Manufacturer;
import com.mastersessay.blockchain.accounting.model.dictionary.facility.Fan;
import com.mastersessay.blockchain.accounting.model.dictionary.facility.MiningCoolingRack;
import com.mastersessay.blockchain.accounting.repository.FanRepository;
import com.mastersessay.blockchain.accounting.repository.ManufacturerRepository;
import com.mastersessay.blockchain.accounting.repository.MiningCoolingRackRepository;
import com.mastersessay.blockchain.accounting.repository.UserRepository;
import com.mastersessay.blockchain.accounting.util.PageUtils;
import com.mastersessay.blockchain.accounting.util.converters.FanUtils;
import com.mastersessay.blockchain.accounting.util.converters.ManufacturerUtils;
import com.mastersessay.blockchain.accounting.util.converters.MiningCoolingRackUtils;
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
public class FanService {
    private static final Logger log = LoggerFactory.getLogger(FanService.class);

    private final FanRepository fanRepository;
    private final ManufacturerService manufacturerService;
    private final FanUtils fanUtils;

    private final GenericCrudProcessor<Long, Fan, FanRequest, FanResponse> miningCoolingRackCrudProcessor;

    @Transactional
    public FanResponse getById(Long id) {
        return miningCoolingRackCrudProcessor.getById(
                id,
                fanRepository,
                fanUtils
        );
    }

    @Transactional
    public List<FanResponse> getAll(Integer start,
                                    Integer count,
                                    String sortBy,
                                    String sortType) {
        return miningCoolingRackCrudProcessor.getAll(
                start,
                count,
                sortBy,
                sortType,
                fanRepository,
                fanUtils
        );
    }

    @Transactional
    public FanResponse create(FanRequest fanRequest, UserDetails user) {
        Optional<Fan> existingFan = fanRepository.getByModel(fanRequest.getModel());

        existingFan.ifPresent(fan -> {
            if (fan.getManufacturer().getName()
                    .equals(fanRequest.getManufacturer().getName())) {
                throw new IllegalArgumentException(OBJECT_EXISTS_MESSAGE);
            }
        });

        Manufacturer manufacturer = manufacturerService.findOrCreateByName(fanRequest.getManufacturer(), user);

        return miningCoolingRackCrudProcessor.create(
                fanRequest,
                user,
                manufacturer,
                fanRepository,
                fanUtils
        );
    }

    @Transactional
    public FanResponse update(Long id, FanRequest fanRequest, UserDetails user) {
        return miningCoolingRackCrudProcessor.update(
                id,
                fanRequest,
                manufacturerService.getByName(fanRequest.getManufacturer().getName()),
                user,
                fanRepository,
                fanUtils
        );
    }

    @Transactional
    public void deleteById(Long id) {
        miningCoolingRackCrudProcessor.deleteById(id, fanRepository);
    }

    @Autowired

    public FanService(FanRepository fanRepository,
                      ManufacturerService manufacturerService,
                      FanUtils fanUtils,
                      GenericCrudProcessor<Long, Fan, FanRequest, FanResponse> miningCoolingRackCrudProcessor) {
        this.fanRepository = fanRepository;
        this.manufacturerService = manufacturerService;
        this.fanUtils = fanUtils;
        this.miningCoolingRackCrudProcessor = miningCoolingRackCrudProcessor;
    }
}
