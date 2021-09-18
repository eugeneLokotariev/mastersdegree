package com.mastersessay.blockchain.accounting.service;

import com.mastersessay.blockchain.accounting.dto.request.facility.FanRequest;
import com.mastersessay.blockchain.accounting.dto.response.facility.FanResponse;
import com.mastersessay.blockchain.accounting.model.dictionary.Manufacturer;
import com.mastersessay.blockchain.accounting.model.dictionary.facility.Fan;
import com.mastersessay.blockchain.accounting.repository.FanRepository;
import com.mastersessay.blockchain.accounting.util.converters.FanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.mastersessay.blockchain.accounting.consts.BlockchainAccountingConstants.BusinessMessages.OBJECT_EXISTS_MESSAGE;

@SuppressWarnings("Duplicates")
@Service
public class FanService {
    private static final Logger log = LoggerFactory.getLogger(FanService.class);

    private final FanRepository fanRepository;
    private final ManufacturerService manufacturerService;
    private final FanUtils fanUtils;

    private final GenericCrudProcessor<Long, Fan, FanRequest, FanResponse> fanCrudProcessor;

    @Transactional
    public FanResponse getById(Long id) {
        return fanCrudProcessor.getById(
                id,
                fanRepository,
                fanUtils
        );
    }

    @Transactional
    public List<FanResponse> getByMatchingName(String name) {
        return fanCrudProcessor.getAllByMatchingName(name, fanRepository, fanUtils);
    }

    @Transactional
    public List<FanResponse> getAll(Integer start,
                                    Integer count,
                                    String sortBy,
                                    String sortType) {
        return fanCrudProcessor.getAll(
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

        return fanCrudProcessor.create(
                fanRequest,
                user,
                manufacturer,
                fanRepository,
                fanUtils
        );
    }

    @Transactional
    public FanResponse update(Long id, FanRequest fanRequest, UserDetails user) {
        return fanCrudProcessor.update(
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
        fanCrudProcessor.deleteById(id, fanRepository);
    }

    @Autowired

    public FanService(FanRepository fanRepository,
                      ManufacturerService manufacturerService,
                      FanUtils fanUtils,
                      GenericCrudProcessor<Long, Fan, FanRequest, FanResponse> fanCrudProcessor) {
        this.fanRepository = fanRepository;
        this.manufacturerService = manufacturerService;
        this.fanUtils = fanUtils;
        this.fanCrudProcessor = fanCrudProcessor;
    }
}
