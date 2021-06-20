package com.mastersessay.blockchain.accounting.dto.response.facility;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Data
@Builder
public class MiningCoolingResponse implements Serializable {
    private Long id;

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

    private ManufacturerResponse manufacturer;

    private String createdWhen;

    private String createdBy;

    private String modifiedWhen;

    private String modifiedBy;
}
