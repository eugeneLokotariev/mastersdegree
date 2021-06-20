package com.mastersessay.blockchain.accounting.service;

import com.mastersessay.blockchain.accounting.dto.request.facility.AirConditioningDeviceRequest;
import com.mastersessay.blockchain.accounting.dto.response.facility.AirConditioningDeviceResponse;
import com.mastersessay.blockchain.accounting.model.dictionary.facility.AirConditioningDevice;
import com.mastersessay.blockchain.accounting.repository.AirConditioningDeviceRepository;
import com.mastersessay.blockchain.accounting.util.converters.AirConditioningDeviceUtils;
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
public class AirConditioningDeviceService {
    private static final Logger log = LoggerFactory.getLogger(FanService.class);

    private final AirConditioningDeviceRepository airConditioningDeviceRepository;
    private final ManufacturerService manufacturerService;
    private final AirConditioningDeviceUtils airConditioningDeviceUtils;

    private final GenericCrudProcessor<Long, AirConditioningDevice, AirConditioningDeviceRequest, AirConditioningDeviceResponse> airHandlingCrudProcessor;

    @Transactional
    public AirConditioningDeviceResponse getById(Long id) {
        return airHandlingCrudProcessor.getById(
                id,
                airConditioningDeviceRepository,
                airConditioningDeviceUtils
        );
    }

    @Transactional
    public List<AirConditioningDeviceResponse> getAll(Integer start,
                                                      Integer count,
                                                      String sortBy,
                                                      String sortType) {
        return airHandlingCrudProcessor.getAll(
                start,
                count,
                sortBy,
                sortType,
                airConditioningDeviceRepository,
                airConditioningDeviceUtils
        );
    }

    @Transactional
    public AirConditioningDeviceResponse create(AirConditioningDeviceRequest airConditioningDeviceRequest,
                                                UserDetails user) {
        Optional<AirConditioningDevice> existingUnit =
                airConditioningDeviceRepository.getByModel(airConditioningDeviceRequest.getModel());

        existingUnit.ifPresent(fan -> {
            if (fan.getManufacturer().getName()
                    .equals(airConditioningDeviceRequest.getManufacturer().getName())) {
                throw new IllegalArgumentException(OBJECT_EXISTS_MESSAGE);
            }
        });

        return airHandlingCrudProcessor.create(
                airConditioningDeviceRequest,
                user,
                manufacturerService.findOrCreateByName(airConditioningDeviceRequest.getManufacturer(), user),
                airConditioningDeviceRepository,
                airConditioningDeviceUtils
        );
    }

    @Transactional
    public AirConditioningDeviceResponse update(Long id,
                                                AirConditioningDeviceRequest airConditioningDeviceRequest,
                                                UserDetails user) {
        return airHandlingCrudProcessor.update(
                id,
                airConditioningDeviceRequest,
                manufacturerService.getByName(airConditioningDeviceRequest.getManufacturer().getName()),
                user,
                airConditioningDeviceRepository,
                airConditioningDeviceUtils
        );
    }

    @Transactional
    public void deleteById(Long id) {
        airHandlingCrudProcessor.deleteById(id, airConditioningDeviceRepository);
    }

    @Autowired
    public AirConditioningDeviceService(AirConditioningDeviceRepository airConditioningDeviceRepository,
                                        ManufacturerService manufacturerService,
                                        AirConditioningDeviceUtils airConditioningDeviceUtils,
                                        GenericCrudProcessor<Long, AirConditioningDevice, AirConditioningDeviceRequest, AirConditioningDeviceResponse> airHandlingCrudProcessor) {
        this.airConditioningDeviceRepository = airConditioningDeviceRepository;
        this.manufacturerService = manufacturerService;
        this.airConditioningDeviceUtils = airConditioningDeviceUtils;
        this.airHandlingCrudProcessor = airHandlingCrudProcessor;
    }
}
