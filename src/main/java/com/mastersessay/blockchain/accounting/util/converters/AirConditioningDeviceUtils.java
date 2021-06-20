package com.mastersessay.blockchain.accounting.util.converters;

import com.mastersessay.blockchain.accounting.dto.request.facility.AirConditioningDeviceRequest;
import com.mastersessay.blockchain.accounting.dto.response.facility.AirConditioningDeviceResponse;
import com.mastersessay.blockchain.accounting.model.dictionary.Manufacturer;
import com.mastersessay.blockchain.accounting.model.dictionary.facility.AirConditioningDevice;
import com.mastersessay.blockchain.accounting.model.user.User;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
@SuppressWarnings("Duplicates")
public class AirConditioningDeviceUtils implements GenericConvertable<AirConditioningDevice, AirConditioningDeviceRequest, AirConditioningDeviceResponse> {
    private final ManufacturerUtils manufacturerUtils;

    public AirConditioningDeviceResponse buildResponseFromEntity(AirConditioningDevice airConditioningDevice) {
        return AirConditioningDeviceResponse
                .builder()
                .id(airConditioningDevice.getId())
                .model(StringUtils.isEmpty(airConditioningDevice.getModel()) ? "" : airConditioningDevice.getModel())
                .power(StringUtils.isEmpty(airConditioningDevice.getPower()) ? "" : airConditioningDevice.getPower())
                .noiseLevel(StringUtils.isEmpty(airConditioningDevice.getNoiseLevel()) ? "" : airConditioningDevice.getNoiseLevel())
                .weight(StringUtils.isEmpty(airConditioningDevice.getWeight()) ? "" : airConditioningDevice.getWeight())
                .size(StringUtils.isEmpty(airConditioningDevice.getSize()) ? "" : airConditioningDevice.getSize())
                .voltage(StringUtils.isEmpty(airConditioningDevice.getVoltage()) ? "" : airConditioningDevice.getVoltage())
                .priceUsd(airConditioningDevice.getPriceUsd() == null ? 0 : airConditioningDevice.getPriceUsd())
                .webReference(StringUtils.isEmpty(airConditioningDevice.getWebReference()) ? "" : airConditioningDevice.getWebReference())
                .airConditioningDevice(airConditioningDevice.getAirConditioningDeviceType())
                .coolingCapacity(StringUtils.isEmpty(airConditioningDevice.getCoolingCapacity()) ? "" : airConditioningDevice.getCoolingCapacity())
                .roomAreaSquareM(airConditioningDevice.getRoomAreaSquareM() == null ? 0 : airConditioningDevice.getRoomAreaSquareM())
                .manufacturer(manufacturerUtils.buildResponseFromEntity(airConditioningDevice.getManufacturer()))
                .createdWhen(StringUtils.isEmpty(airConditioningDevice.getCreatedWhen()) ? "" : airConditioningDevice.getCreatedWhen())
                .createdBy(StringUtils.isEmpty(airConditioningDevice.getCreatedBy()) ? "" : airConditioningDevice.getCreatedBy())
                .modifiedWhen(StringUtils.isEmpty(airConditioningDevice.getModifiedWhen()) ? "" : airConditioningDevice.getModifiedWhen())
                .modifiedBy(StringUtils.isEmpty(airConditioningDevice.getModifiedBy()) ? "" : airConditioningDevice.getModifiedBy())
                .build();
    }

    public AirConditioningDevice formEntityFromRequest(AirConditioningDeviceRequest airConditioningDeviceRequest,
                                                       Manufacturer manufacturer,
                                                       User userDetails) {
        return AirConditioningDevice
                .builder()
                .model(airConditioningDeviceRequest.getModel())
                .power(airConditioningDeviceRequest.getPower())
                .noiseLevel(airConditioningDeviceRequest.getNoiseLevel())
                .weight(airConditioningDeviceRequest.getWeight())
                .size(airConditioningDeviceRequest.getSize())
                .voltage(airConditioningDeviceRequest.getVoltage())
                .priceUsd(airConditioningDeviceRequest.getPriceUsd())
                .webReference(airConditioningDeviceRequest.getWebReference())
                .airConditioningDeviceType(airConditioningDeviceRequest.getAirConditioningDevice())
                .coolingCapacity(airConditioningDeviceRequest.getCoolingCapacity())
                .roomAreaSquareM(airConditioningDeviceRequest.getRoomAreaSquareM())
                .manufacturer(manufacturer)
                .createdBy(userDetails.getUsername())
                .createdWhen(LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy hh:mm")))
                .build();
    }

    public void updateValues(AirConditioningDevice airConditioningDevice,
                             AirConditioningDeviceRequest airConditioningDeviceRequest,
                             Manufacturer manufacturer,
                             User user) {
        airConditioningDevice.setModel(airConditioningDeviceRequest.getModel());
        airConditioningDevice.setPower(airConditioningDeviceRequest.getPower());
        airConditioningDevice.setNoiseLevel(airConditioningDeviceRequest.getNoiseLevel());
        airConditioningDevice.setWeight(airConditioningDeviceRequest.getWeight());
        airConditioningDevice.setSize(airConditioningDeviceRequest.getSize());
        airConditioningDevice.setVoltage(airConditioningDeviceRequest.getVoltage());
        airConditioningDevice.setPriceUsd(airConditioningDeviceRequest.getPriceUsd());
        airConditioningDevice.setWebReference(airConditioningDeviceRequest.getWebReference());
        airConditioningDevice.setAirConditioningDeviceType(airConditioningDeviceRequest.getAirConditioningDevice());
        airConditioningDevice.setCoolingCapacity(airConditioningDeviceRequest.getCoolingCapacity());
        airConditioningDevice.setRoomAreaSquareM(airConditioningDeviceRequest.getRoomAreaSquareM());
        airConditioningDevice.setManufacturer(manufacturer);
        airConditioningDevice.setModifiedWhen(user.getUsername());
        airConditioningDevice.setModifiedBy(LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy hh:mm")));
    }

    @Autowired
    public AirConditioningDeviceUtils(ManufacturerUtils manufacturerUtils) {
        this.manufacturerUtils = manufacturerUtils;
    }
}
