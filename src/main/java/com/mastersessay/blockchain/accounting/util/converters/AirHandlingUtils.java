package com.mastersessay.blockchain.accounting.util.converters;

import com.mastersessay.blockchain.accounting.dto.request.facility.AirHandlingUnitRequest;
import com.mastersessay.blockchain.accounting.dto.request.facility.FanRequest;
import com.mastersessay.blockchain.accounting.dto.response.facility.AirHandlingUnitResponse;
import com.mastersessay.blockchain.accounting.dto.response.facility.FanResponse;
import com.mastersessay.blockchain.accounting.model.dictionary.Manufacturer;
import com.mastersessay.blockchain.accounting.model.dictionary.facility.AirHandlingUnit;
import com.mastersessay.blockchain.accounting.model.dictionary.facility.Fan;
import com.mastersessay.blockchain.accounting.model.user.User;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
@SuppressWarnings("Duplicates")
public class AirHandlingUtils implements GenericConvertable<AirHandlingUnit, AirHandlingUnitRequest, AirHandlingUnitResponse>  {
    private final ManufacturerUtils manufacturerUtils;

    public AirHandlingUnitResponse buildResponseFromEntity(AirHandlingUnit airHandlingUnit) {
        return AirHandlingUnitResponse
                .builder()
                .id(airHandlingUnit.getId())
                .model(StringUtils.isEmpty(airHandlingUnit.getModel()) ? "" : airHandlingUnit.getModel())
                .power(StringUtils.isEmpty(airHandlingUnit.getPower()) ? "" : airHandlingUnit.getPower())
                .noiseLevel(StringUtils.isEmpty(airHandlingUnit.getNoiseLevel()) ? "" : airHandlingUnit.getNoiseLevel())
                .weight(StringUtils.isEmpty(airHandlingUnit.getWeight()) ? "" : airHandlingUnit.getWeight())
                .size(StringUtils.isEmpty(airHandlingUnit.getSize()) ? "" : airHandlingUnit.getSize())
                .voltage(StringUtils.isEmpty(airHandlingUnit.getVoltage()) ? "" : airHandlingUnit.getVoltage())
                .priceUsd(airHandlingUnit.getPriceUsd() == null ? 0 : airHandlingUnit.getPriceUsd())
                .webReference(StringUtils.isEmpty(airHandlingUnit.getWebReference()) ? "" : airHandlingUnit.getWebReference())
                .manufacturer(manufacturerUtils.buildResponseFromEntity(airHandlingUnit.getManufacturer()))
                .pipeDiameter(StringUtils.isEmpty(airHandlingUnit.getPipeDiameter()) ? "" : airHandlingUnit.getPipeDiameter())
                .ventilatedArea(StringUtils.isEmpty(airHandlingUnit.getVentilatedArea()) ? "" : airHandlingUnit.getVentilatedArea())
                .performance(StringUtils.isEmpty(airHandlingUnit.getPerformance()) ? "" : airHandlingUnit.getPerformance())
                .createdWhen(StringUtils.isEmpty(airHandlingUnit.getCreatedWhen()) ? "" : airHandlingUnit.getCreatedWhen())
                .createdBy(StringUtils.isEmpty(airHandlingUnit.getCreatedBy()) ? "" : airHandlingUnit.getCreatedBy())
                .modifiedWhen(StringUtils.isEmpty(airHandlingUnit.getModifiedWhen()) ? "" : airHandlingUnit.getModifiedWhen())
                .modifiedBy(StringUtils.isEmpty(airHandlingUnit.getModifiedBy()) ? "" : airHandlingUnit.getModifiedBy())
                .build();
    }

    public AirHandlingUnit formEntityFromRequest(AirHandlingUnitRequest airHandlingUnitRequest,
                                                 Manufacturer manufacturer,
                                                 User userDetails) {
        return AirHandlingUnit
                .builder()
                .model(airHandlingUnitRequest.getModel())
                .power(airHandlingUnitRequest.getPower())
                .noiseLevel(airHandlingUnitRequest.getNoiseLevel())
                .weight(airHandlingUnitRequest.getWeight())
                .size(airHandlingUnitRequest.getSize())
                .voltage(airHandlingUnitRequest.getVoltage())
                .priceUsd(airHandlingUnitRequest.getPriceUsd())
                .webReference(airHandlingUnitRequest.getWebReference())
                .pipeDiameter(airHandlingUnitRequest.getPipeDiameter())
                .ventilatedArea(airHandlingUnitRequest.getVentilatedArea())
                .performance(airHandlingUnitRequest.getPerformance())
                .manufacturer(manufacturer)
                .createdBy(userDetails.getUsername())
                .createdWhen(LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy hh:mm")))
                .build();
    }

    public void updateValues(AirHandlingUnit airHandlingUnit,
                             AirHandlingUnitRequest airHandlingUnitRequest,
                             Manufacturer manufacturer,
                             User user) {
        airHandlingUnit.setModel(airHandlingUnitRequest.getModel());
        airHandlingUnit.setPower(airHandlingUnitRequest.getPower());
        airHandlingUnit.setNoiseLevel(airHandlingUnitRequest.getNoiseLevel());
        airHandlingUnit.setWeight(airHandlingUnitRequest.getWeight());
        airHandlingUnit.setSize(airHandlingUnitRequest.getSize());
        airHandlingUnit.setVoltage(airHandlingUnitRequest.getVoltage());
        airHandlingUnit.setPriceUsd(airHandlingUnitRequest.getPriceUsd());
        airHandlingUnit.setWebReference(airHandlingUnitRequest.getWebReference());
        airHandlingUnit.setPipeDiameter(airHandlingUnitRequest.getPipeDiameter());
        airHandlingUnit.setVentilatedArea(airHandlingUnitRequest.getVentilatedArea());
        airHandlingUnit.setPerformance(airHandlingUnitRequest.getPerformance());
        airHandlingUnit.setManufacturer(manufacturer);
        airHandlingUnit.setModifiedWhen(user.getUsername());
        airHandlingUnit.setModifiedBy(LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy hh:mm")));
    }

    @Autowired
    public AirHandlingUtils(ManufacturerUtils manufacturerUtils) {
        this.manufacturerUtils = manufacturerUtils;
    }
}
