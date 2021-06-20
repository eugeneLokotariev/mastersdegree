package com.mastersessay.blockchain.accounting.dto.response.facility;

import com.mastersessay.blockchain.accounting.consts.AirConditioningDeviceType;
import com.mastersessay.blockchain.accounting.dto.request.facility.ManufacturerRequest;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AirConditioningDeviceResponse {
    private Long id;

    private String model;

    private String power;

    private String noiseLevel;

    private String weight;

    private String size;

    private String voltage;

    private Integer priceUsd;

    private String webReference;

    private AirConditioningDeviceType airConditioningDevice;

    private String coolingCapacity;

    private Integer roomAreaSquareM;

    private ManufacturerResponse manufacturer;

    private String createdWhen;

    private String createdBy;

    private String modifiedWhen;

    private String modifiedBy;
}
