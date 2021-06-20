package com.mastersessay.blockchain.accounting.service;

import com.mastersessay.blockchain.accounting.dto.request.facility.MiningCoolingRackRequest;
import com.mastersessay.blockchain.accounting.dto.request.facility.MiningFarmRequest;
import com.mastersessay.blockchain.accounting.dto.response.facility.MiningCoolingResponse;
import com.mastersessay.blockchain.accounting.dto.response.facility.MiningFarmResponse;
import com.mastersessay.blockchain.accounting.model.dictionary.Manufacturer;
import com.mastersessay.blockchain.accounting.model.dictionary.facility.MiningCoolingRack;
import com.mastersessay.blockchain.accounting.model.dictionary.facility.MiningFarm;
import com.mastersessay.blockchain.accounting.repository.ManufacturerRepository;
import com.mastersessay.blockchain.accounting.repository.MiningCoolingRackRepository;
import com.mastersessay.blockchain.accounting.repository.MiningFarmRepository;
import com.mastersessay.blockchain.accounting.repository.UserRepository;
import com.mastersessay.blockchain.accounting.util.PageUtils;
import com.mastersessay.blockchain.accounting.util.converters.ManufacturerUtils;
import com.mastersessay.blockchain.accounting.util.converters.MiningCoolingRackUtils;
import com.mastersessay.blockchain.accounting.util.converters.MiningFarmUtils;
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
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static com.mastersessay.blockchain.accounting.consts.BlockchainAccountingConstants.BusinessMessages.*;

@SuppressWarnings("Duplicates")
@Service
public class MiningCoolingRackService {
    private static final Logger log = LoggerFactory.getLogger(MiningFarmService.class);

    private final MiningCoolingRackRepository miningCoolingRackRepository;
    private final ManufacturerService manufacturerService;
    private final MiningCoolingRackUtils miningCoolingRackUtils;

    private final GenericCrudProcessor<Long, MiningCoolingRack, MiningCoolingRackRequest, MiningCoolingResponse> miningCoolingRackCrudProcessor;

    @Transactional
    public MiningCoolingResponse getById(Long id) {
        return miningCoolingRackCrudProcessor.getById(
                id,
                miningCoolingRackRepository,
                miningCoolingRackUtils
        );
    }

    @Transactional
    public List<MiningCoolingResponse> getAll(Integer start,
                                              Integer count,
                                              String sortBy,
                                              String sortType) {
        return miningCoolingRackCrudProcessor.getAll(
                start,
                count,
                sortBy,
                sortType,
                miningCoolingRackRepository,
                miningCoolingRackUtils
        );
    }

    @Transactional
    public MiningCoolingResponse create(MiningCoolingRackRequest miningCoolingRackRequest, UserDetails user) {
        Optional<MiningCoolingRack> existingMiningCoolingRackService = miningCoolingRackRepository
                .getByModel(miningCoolingRackRequest.getModel());

        existingMiningCoolingRackService.ifPresent(miningCoolingRack -> {
            if (miningCoolingRack.getManufacturer().getName()
                    .equals(miningCoolingRackRequest.getManufacturer().getName())) {
                throw new IllegalArgumentException(OBJECT_EXISTS_MESSAGE);
            }
        });

        return miningCoolingRackCrudProcessor.create(
                miningCoolingRackRequest,
                user,
                manufacturerService.findOrCreateByName(miningCoolingRackRequest.getManufacturer(), user),
                miningCoolingRackRepository,
                miningCoolingRackUtils
        );
    }

    @Transactional
    public MiningCoolingResponse update(Long id, MiningCoolingRackRequest miningCoolingRackRequest, UserDetails user) {
        Manufacturer manufacturer = manufacturerService.getByName(miningCoolingRackRequest.getManufacturer().getName());

        return miningCoolingRackCrudProcessor.update(
                id,
                miningCoolingRackRequest,
                manufacturer,
                user,
                miningCoolingRackRepository,
                miningCoolingRackUtils
        );
    }

    @Transactional
    public void deleteById(Long id) {
        miningCoolingRackCrudProcessor.deleteById(id, miningCoolingRackRepository);
    }

    @Autowired
    public MiningCoolingRackService(MiningCoolingRackRepository miningCoolingRackRepository, ManufacturerService manufacturerService, MiningCoolingRackUtils miningCoolingRackUtils, GenericCrudProcessor<Long, MiningCoolingRack, MiningCoolingRackRequest, MiningCoolingResponse> miningCoolingRackCrudProcessor) {
        this.miningCoolingRackRepository = miningCoolingRackRepository;
        this.manufacturerService = manufacturerService;
        this.miningCoolingRackUtils = miningCoolingRackUtils;
        this.miningCoolingRackCrudProcessor = miningCoolingRackCrudProcessor;
    }
}
