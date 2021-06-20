package com.mastersessay.blockchain.accounting.service;

import com.mastersessay.blockchain.accounting.dto.request.facility.MiningFarmRequest;
import com.mastersessay.blockchain.accounting.dto.response.facility.ManufacturerResponse;
import com.mastersessay.blockchain.accounting.dto.response.facility.MiningFarmResponse;
import com.mastersessay.blockchain.accounting.model.dictionary.Manufacturer;
import com.mastersessay.blockchain.accounting.model.dictionary.facility.MiningFarm;
import com.mastersessay.blockchain.accounting.repository.MiningFarmRepository;
import com.mastersessay.blockchain.accounting.util.converters.MiningFarmUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

import static com.mastersessay.blockchain.accounting.consts.BlockchainAccountingConstants.BusinessMessages.OBJECT_EXISTS_MESSAGE;

@Service
@SuppressWarnings("Duplicates")
public class MiningFarmService {
    private static final Logger log = LoggerFactory.getLogger(MiningFarmService.class);

    private final MiningFarmRepository miningFarmRepository;
    private final ManufacturerService manufacturerService;
    private final MiningFarmUtils miningFarmUtils;

    private final GenericCrudProcessor<Long, MiningFarm, MiningFarmRequest, MiningFarmResponse> miningFarmCrudProcessor;

    @Transactional
    public MiningFarmResponse getById(Long id) {
        return miningFarmCrudProcessor.getById(
                id,
                miningFarmRepository,
                miningFarmUtils
        );
    }

    @Transactional
    public List<MiningFarmResponse> getAll(Integer start,
                                           Integer count,
                                           String sortBy,
                                           String sortType) {
        return miningFarmCrudProcessor.getAll(
                start,
                count,
                sortBy,
                sortType,
                miningFarmRepository,
                miningFarmUtils
        );
    }

    @Transactional
    public MiningFarmResponse create(MiningFarmRequest miningFarmRequest, UserDetails user) {
        Optional<MiningFarm> existingMiningFarm = miningFarmRepository
                .getByModel(miningFarmRequest.getModel());

        existingMiningFarm.ifPresent(miningFarm -> {
            if (miningFarm.getManufacturer().getName()
                    .equals(miningFarmRequest.getManufacturer().getName())) {
                throw new IllegalArgumentException(OBJECT_EXISTS_MESSAGE);
            }
        });

        return miningFarmCrudProcessor.create(
                miningFarmRequest,
                user,
                manufacturerService.findOrCreateByName(miningFarmRequest.getManufacturer(), user),
                miningFarmRepository,
                miningFarmUtils
        );
    }

    @Transactional
    public MiningFarmResponse update(Long id,
                                     MiningFarmRequest miningFarmRequest,
                                     UserDetails user) {
        return miningFarmCrudProcessor.update(
                id,
                miningFarmRequest,
                manufacturerService.getByName(miningFarmRequest.getManufacturer().getName()),
                user,
                miningFarmRepository,
                miningFarmUtils
        );
    }

    @Transactional
    public void deleteById(Long id) {
        miningFarmCrudProcessor.deleteById(id, miningFarmRepository);
    }

    @Autowired

    public MiningFarmService(MiningFarmRepository miningFarmRepository,
                             ManufacturerService manufacturerService,
                             MiningFarmUtils miningFarmUtils,
                             GenericCrudProcessor<Long, MiningFarm, MiningFarmRequest, MiningFarmResponse> miningFarmCrudProcessor) {
        this.miningFarmRepository = miningFarmRepository;
        this.manufacturerService = manufacturerService;
        this.miningFarmUtils = miningFarmUtils;
        this.miningFarmCrudProcessor = miningFarmCrudProcessor;
    }
}
