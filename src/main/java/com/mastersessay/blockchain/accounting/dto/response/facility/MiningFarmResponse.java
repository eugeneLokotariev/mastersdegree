package com.mastersessay.blockchain.accounting.dto.response.facility;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Data
@Builder
public class MiningFarmResponse implements Serializable {
    private Long id;

    private String model;

    private String alsoAsKnownAs;

    private String releaseDate;

    private String size;

    private String weight;

    private String noiseLevel;

    private Integer fans;

    private Integer chipCount;

    private String rackFormat;

    private String cooling;

    private String power;

    private String voltage;

    private String interfaceName;

    private String memory;

    private String temperature;

    private String humidity;

    private Integer priceUsd;

    private ManufacturerResponse manufacturer;

    private String createdWhen;

    private String createdBy;

    private String modifiedWhen;

    private String modifiedBy;
}
