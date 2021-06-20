package com.mastersessay.blockchain.accounting.dto.request.facility;

import com.mastersessay.blockchain.accounting.consts.AirConditioningDeviceType;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
@Builder
public class AirConditioningDeviceRequest implements Serializable {
    private String model;

    private String power;

    private String noiseLevel;

    private String weight;

    private String size;

    private String voltage;

    private Integer priceUsd;

    private String webReference;

    @NotBlank
    private AirConditioningDeviceType airConditioningDevice;

    @NotBlank
    private String coolingCapacity;

    @NotNull
    private Integer roomAreaSquareM;

    @NotNull
    private ManufacturerRequest manufacturer;
}
