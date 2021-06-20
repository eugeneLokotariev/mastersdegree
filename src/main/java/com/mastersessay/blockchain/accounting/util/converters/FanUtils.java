package com.mastersessay.blockchain.accounting.util.converters;

import com.mastersessay.blockchain.accounting.dto.request.facility.FanRequest;
import com.mastersessay.blockchain.accounting.dto.response.facility.FanResponse;
import com.mastersessay.blockchain.accounting.model.dictionary.Manufacturer;
import com.mastersessay.blockchain.accounting.model.dictionary.facility.Fan;
import com.mastersessay.blockchain.accounting.model.user.User;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
@SuppressWarnings("Duplicates")
public class FanUtils implements GenericConvertable<Fan, FanRequest, FanResponse> {
    private final ManufacturerUtils manufacturerUtils;

    public FanResponse buildResponseFromEntity(Fan fan) {
        return FanResponse
                .builder()
                .id(fan.getId())
                .model(StringUtils.isEmpty(fan.getModel()) ? "" : fan.getModel())
                .power(StringUtils.isEmpty(fan.getPower()) ? "" : fan.getPower())
                .noiseLevel(StringUtils.isEmpty(fan.getNoiseLevel()) ? "" : fan.getNoiseLevel())
                .weight(StringUtils.isEmpty(fan.getWeight()) ? "" : fan.getWeight())
                .size(StringUtils.isEmpty(fan.getSize()) ? "" : fan.getSize())
                .voltage(StringUtils.isEmpty(fan.getVoltage()) ? "" : fan.getVoltage())
                .priceUsd(fan.getPriceUsd() == null ? 0 : fan.getPriceUsd())
                .webReference(StringUtils.isEmpty(fan.getWebReference()) ? "" : fan.getWebReference())
                .airConsumption(StringUtils.isEmpty(fan.getAirConsumption()) ? "" : fan.getAirConsumption())
                .branchPipeSize(StringUtils.isEmpty(fan.getBranchPipeSize()) ? "" : fan.getBranchPipeSize())
                .currentConsumption(StringUtils.isEmpty(fan.getCurrentConsumption()) ? "" : fan.getCurrentConsumption())
                .manufacturer(manufacturerUtils.buildResponseFromEntity(fan.getManufacturer()))
                .createdWhen(StringUtils.isEmpty(fan.getCreatedWhen()) ? "" : fan.getCreatedWhen())
                .createdBy(StringUtils.isEmpty(fan.getCreatedBy()) ? "" : fan.getCreatedBy())
                .modifiedWhen(StringUtils.isEmpty(fan.getModifiedWhen()) ? "" : fan.getModifiedWhen())
                .modifiedBy(StringUtils.isEmpty(fan.getModifiedBy()) ? "" : fan.getModifiedBy())
                .build();
    }

    public Fan formEntityFromRequest(FanRequest fanRequest,
                                     Manufacturer manufacturer,
                                     User userDetails) {
        return Fan
                .builder()
                .model(fanRequest.getModel())
                .power(fanRequest.getPower())
                .noiseLevel(fanRequest.getNoiseLevel())
                .weight(fanRequest.getWeight())
                .size(fanRequest.getSize())
                .voltage(fanRequest.getVoltage())
                .priceUsd(fanRequest.getPriceUsd())
                .webReference(fanRequest.getWebReference())
                .airConsumption(fanRequest.getAirConsumption())
                .branchPipeSize(fanRequest.getBranchPipeSize())
                .currentConsumption(fanRequest.getCurrentConsumption())
                .manufacturer(manufacturer)
                .createdBy(userDetails.getUsername())
                .createdWhen(LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy hh:mm")))
                .build();
    }

    public void updateValues(Fan fan,
                             FanRequest fanRequest,
                             Manufacturer manufacturer,
                             User user) {
        fan.setModel(fanRequest.getModel());
        fan.setPower(fanRequest.getPower());
        fan.setNoiseLevel(fanRequest.getNoiseLevel());
        fan.setWeight(fanRequest.getWeight());
        fan.setSize(fanRequest.getSize());
        fan.setVoltage(fanRequest.getVoltage());
        fan.setPriceUsd(fanRequest.getPriceUsd());
        fan.setWebReference(fanRequest.getWebReference());
        fan.setAirConsumption(fanRequest.getAirConsumption());
        fan.setBranchPipeSize(fanRequest.getBranchPipeSize());
        fan.setCurrentConsumption(fanRequest.getCurrentConsumption());
        fan.setManufacturer(manufacturer);
        fan.setModifiedWhen(user.getUsername());
        fan.setModifiedBy(LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy hh:mm")));
    }

    @Autowired
    public FanUtils(ManufacturerUtils manufacturerUtils) {
        this.manufacturerUtils = manufacturerUtils;
    }
}
