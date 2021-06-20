package com.mastersessay.blockchain.accounting.dto.request.facility;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
@Builder
public class MiningCoolingRackRequest implements Serializable {
    private String model;

    private String power;

    private String noiseLevel;

    private String weight;

    private String size;

    private String voltage;

    private Integer priceUsd;

    private String webReference;

    private Integer waterCapacity;

    private String optimalWaterConsumption;

    private String maxCoolingPower;

    private String pumpConsumption;

    @NotNull
    private ManufacturerRequest manufacturer;

    private String createdWhen;

    private String createdBy;

    private String modifiedWhen;

    private String modifiedBy;
}
