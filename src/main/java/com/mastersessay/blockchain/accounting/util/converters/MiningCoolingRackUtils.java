package com.mastersessay.blockchain.accounting.util.converters;

import com.mastersessay.blockchain.accounting.dto.request.facility.MiningCoolingRackRequest;
import com.mastersessay.blockchain.accounting.dto.request.facility.MiningFarmRequest;
import com.mastersessay.blockchain.accounting.dto.response.facility.MiningCoolingResponse;
import com.mastersessay.blockchain.accounting.dto.response.facility.MiningFarmResponse;
import com.mastersessay.blockchain.accounting.model.dictionary.Manufacturer;
import com.mastersessay.blockchain.accounting.model.dictionary.facility.MiningCoolingRack;
import com.mastersessay.blockchain.accounting.model.dictionary.facility.MiningFarm;
import com.mastersessay.blockchain.accounting.model.user.User;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
@SuppressWarnings("Duplicates")
public class MiningCoolingRackUtils implements GenericConvertable<MiningCoolingRack, MiningCoolingRackRequest, MiningCoolingResponse> {
    private final ManufacturerUtils manufacturerUtils;

    public MiningCoolingResponse buildResponseFromEntity(MiningCoolingRack miningCoolingRack) {
        return MiningCoolingResponse
                .builder()
                .id(miningCoolingRack.getId())
                .model(StringUtils.isEmpty(miningCoolingRack.getModel()) ? "" : miningCoolingRack.getModel())
                .power(StringUtils.isEmpty(miningCoolingRack.getPower()) ? "" : miningCoolingRack.getPower())
                .noiseLevel(StringUtils.isEmpty(miningCoolingRack.getNoiseLevel()) ? "" : miningCoolingRack.getNoiseLevel())
                .weight(StringUtils.isEmpty(miningCoolingRack.getWeight()) ? "" : miningCoolingRack.getWeight())
                .size(StringUtils.isEmpty(miningCoolingRack.getSize()) ? "" : miningCoolingRack.getSize())
                .voltage(StringUtils.isEmpty(miningCoolingRack.getVoltage()) ? "" : miningCoolingRack.getVoltage())
                .priceUsd(miningCoolingRack.getPriceUsd() == null ? 0 : miningCoolingRack.getPriceUsd())
                .webReference(StringUtils.isEmpty(miningCoolingRack.getWebReference()) ? "" : miningCoolingRack.getWebReference())
                .waterCapacity(miningCoolingRack.getWaterCapacity() == null ? 0 : miningCoolingRack.getWaterCapacity())
                .optimalWaterConsumption(StringUtils.isEmpty(miningCoolingRack.getOptimalWaterConsumption()) ? "" : miningCoolingRack.getOptimalWaterConsumption())
                .maxCoolingPower(StringUtils.isEmpty(miningCoolingRack.getMaxCoolingPower()) ? "" : miningCoolingRack.getMaxCoolingPower())
                .pumpConsumption(StringUtils.isEmpty(miningCoolingRack.getPumpConsumption()) ? "" : miningCoolingRack.getPumpConsumption())
                .manufacturer(manufacturerUtils.buildResponseFromEntity(miningCoolingRack.getManufacturer()))
                .createdWhen(StringUtils.isEmpty(miningCoolingRack.getCreatedWhen()) ? "" : miningCoolingRack.getCreatedWhen())
                .createdBy(StringUtils.isEmpty(miningCoolingRack.getCreatedBy()) ? "" : miningCoolingRack.getCreatedBy())
                .modifiedWhen(StringUtils.isEmpty(miningCoolingRack.getModifiedWhen()) ? "" : miningCoolingRack.getModifiedWhen())
                .modifiedBy(StringUtils.isEmpty(miningCoolingRack.getModifiedBy()) ? "" : miningCoolingRack.getModifiedBy())
                .build();
    }

    public MiningCoolingRack formEntityFromRequest(MiningCoolingRackRequest request,
                                                   Manufacturer manufacturer,
                                                   User userDetails) {
        return MiningCoolingRack
                .builder()
                .model(request.getModel())
                .power(request.getPower())
                .noiseLevel(request.getNoiseLevel())
                .weight(request.getWeight())
                .size(request.getSize())
                .voltage(request.getVoltage())
                .priceUsd(request.getPriceUsd())
                .webReference(request.getWebReference())
                .waterCapacity(request.getWaterCapacity())
                .optimalWaterConsumption(request.getOptimalWaterConsumption())
                .maxCoolingPower(request.getMaxCoolingPower())
                .pumpConsumption(request.getPumpConsumption())
                .manufacturer(manufacturer)
                .createdBy(userDetails.getUsername())
                .createdWhen(LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy hh:mm")))
                .build();
    }

    public void updateValues(MiningCoolingRack entity,
                             MiningCoolingRackRequest miningCoolingRackRequest,
                             Manufacturer manufacturer,
                             User user) {
        entity.setModel(miningCoolingRackRequest.getModel());
        entity.setPower(miningCoolingRackRequest.getPower());
        entity.setNoiseLevel(miningCoolingRackRequest.getNoiseLevel());
        entity.setWeight(miningCoolingRackRequest.getWeight());
        entity.setSize(miningCoolingRackRequest.getSize());
        entity.setVoltage(miningCoolingRackRequest.getVoltage());
        entity.setPriceUsd(miningCoolingRackRequest.getPriceUsd());
        entity.setWebReference(miningCoolingRackRequest.getWebReference());
        entity.setWaterCapacity(miningCoolingRackRequest.getWaterCapacity());
        entity.setOptimalWaterConsumption(miningCoolingRackRequest.getOptimalWaterConsumption());
        entity.setMaxCoolingPower(miningCoolingRackRequest.getMaxCoolingPower());
        entity.setPumpConsumption(miningCoolingRackRequest.getPumpConsumption());
        entity.setManufacturer(manufacturer);
        entity.setModifiedWhen(user.getUsername());
        entity.setModifiedBy(LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy hh:mm")));
    }

    @Autowired
    public MiningCoolingRackUtils(ManufacturerUtils manufacturerUtils) {
        this.manufacturerUtils = manufacturerUtils;
    }
}
