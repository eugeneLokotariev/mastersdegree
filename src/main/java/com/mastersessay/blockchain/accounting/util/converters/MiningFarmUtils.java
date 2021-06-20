package com.mastersessay.blockchain.accounting.util.converters;

import com.mastersessay.blockchain.accounting.dto.request.facility.MiningFarmRequest;
import com.mastersessay.blockchain.accounting.dto.response.facility.ManufacturerResponse;
import com.mastersessay.blockchain.accounting.dto.response.facility.MiningFarmResponse;
import com.mastersessay.blockchain.accounting.model.dictionary.facility.MiningFarm;
import com.mastersessay.blockchain.accounting.model.dictionary.Manufacturer;
import com.mastersessay.blockchain.accounting.model.user.User;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
@SuppressWarnings("Duplicates")
public class MiningFarmUtils implements GenericConvertable<MiningFarm, MiningFarmRequest, MiningFarmResponse> {
    public MiningFarmResponse buildResponseFromEntity(MiningFarm miningFarm) {
        return MiningFarmResponse
                .builder()
                .id(miningFarm.getId())
                .model(StringUtils.isEmpty(miningFarm.getModel()) ? "" : miningFarm.getModel())
                .alsoAsKnownAs(StringUtils.isEmpty(miningFarm.getAlsoAsKnownAs()) ? "" : miningFarm.getAlsoAsKnownAs())
                .releaseDate(StringUtils.isEmpty(miningFarm.getReleaseDate()) ? "" : miningFarm.getReleaseDate())
                .size(StringUtils.isEmpty(miningFarm.getSize()) ? "" : miningFarm.getSize())
                .weight(StringUtils.isEmpty(miningFarm.getWeight()) ? "" : miningFarm.getWeight())
                .noiseLevel(StringUtils.isEmpty(miningFarm.getNoiseLevel()) ? "" : miningFarm.getNoiseLevel())
                .fans(miningFarm.getFans() == null ? 0 : miningFarm.getFans())
                .chipCount(miningFarm.getChipCount() == null ? 0 : miningFarm.getChipCount())
                .rackFormat(StringUtils.isEmpty(miningFarm.getRackFormat()) ? "" : miningFarm.getRackFormat())
                .cooling(StringUtils.isEmpty(miningFarm.getCooling()) ? "" : miningFarm.getCooling())
                .power(StringUtils.isEmpty(miningFarm.getPower()) ? "" : miningFarm.getPower())
                .voltage(StringUtils.isEmpty(miningFarm.getVoltage()) ? "" : miningFarm.getVoltage())
                .interfaceName(StringUtils.isEmpty(miningFarm.getInterfaceName()) ? "" : miningFarm.getInterfaceName())
                .memory(StringUtils.isEmpty(miningFarm.getMemory()) ? "" : miningFarm.getMemory())
                .temperature(StringUtils.isEmpty(miningFarm.getTemperature()) ? "" : miningFarm.getTemperature())
                .humidity(StringUtils.isEmpty(miningFarm.getHumidity()) ? "" : miningFarm.getHumidity())
                .priceUsd(miningFarm.getPriceUsd())
                .createdBy(miningFarm.getCreatedBy())
                .createdWhen(miningFarm.getCreatedWhen())
                .modifiedBy(miningFarm.getModifiedBy() == null ? "" : miningFarm.getModifiedBy())
                .modifiedWhen(StringUtils.isBlank(miningFarm.getModifiedWhen()) ? "" : miningFarm.getModifiedWhen())
                .manufacturer(ManufacturerResponse
                        .builder()
                        .name(miningFarm.getManufacturer().getName())
                        .id(miningFarm.getManufacturer().getId())
                        .createdBy(miningFarm.getCreatedBy())
                        .createdWhen(miningFarm.getCreatedWhen())
                        .modifiedBy(miningFarm.getModifiedBy() == null ? "" : miningFarm.getModifiedBy())
                        .modifiedWhen(StringUtils.isBlank(miningFarm.getModifiedWhen()) ? "" : miningFarm.getModifiedWhen())
                        .build())
                .build();
    }

    public MiningFarm formEntityFromRequest(MiningFarmRequest request,
                                            Manufacturer manufacturer,
                                            User userDetails) {
        return MiningFarm
                .builder()
                .model(request.getModel())
                .alsoAsKnownAs(request.getAlsoAsKnownAs())
                .releaseDate(request.getReleaseDate().toString())
                .size(request.getSize())
                .weight(request.getWeight())
                .noiseLevel(request.getNoiseLevel())
                .fans(request.getFans())
                .chipCount(request.getChipCount())
                .rackFormat(request.getRackFormat())
                .cooling(request.getCooling())
                .power(request.getPower())
                .voltage(request.getVoltage())
                .interfaceName(request.getInterfaceName())
                .memory(request.getMemory())
                .temperature(request.getTemperature())
                .humidity(request.getHumidity())
                .priceUsd(request.getPriceUsd())
                .manufacturer(manufacturer)
                .createdBy(userDetails.getUsername())
                .createdWhen(LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy hh:mm")))
                .build();
    }

    public void updateValues(MiningFarm entity,
                             MiningFarmRequest miningFarmRequest,
                             Manufacturer manufacturer,
                             User user) {
        entity.setModel(miningFarmRequest.getModel());
        entity.setAlsoAsKnownAs(miningFarmRequest.getAlsoAsKnownAs());
        entity.setReleaseDate(miningFarmRequest.getReleaseDate().toString());
        entity.setSize(miningFarmRequest.getSize());
        entity.setWeight(miningFarmRequest.getWeight());
        entity.setNoiseLevel(miningFarmRequest.getNoiseLevel());
        entity.setFans(miningFarmRequest.getFans());
        entity.setChipCount(miningFarmRequest.getChipCount());
        entity.setRackFormat(miningFarmRequest.getRackFormat());
        entity.setCooling(miningFarmRequest.getCooling());
        entity.setPower(miningFarmRequest.getPower());
        entity.setVoltage(miningFarmRequest.getVoltage());
        entity.setInterfaceName(miningFarmRequest.getInterfaceName());
        entity.setMemory(miningFarmRequest.getMemory());
        entity.setTemperature(miningFarmRequest.getTemperature());
        entity.setHumidity(miningFarmRequest.getHumidity());
        entity.setManufacturer(manufacturer);
        entity.setModifiedBy(user.getUsername());
        entity.setModifiedWhen(LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy hh:mm")));
    }
}
