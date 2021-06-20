package com.mastersessay.blockchain.accounting.dto.request.facility;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
@Builder
public class FanRequest implements Serializable {
    private String model;

    private String power;

    private String noiseLevel;

    private String weight;

    private String size;

    private String voltage;

    private Integer priceUsd;

    private String webReference;

    @NotNull
    private ManufacturerRequest manufacturer;

    @NotBlank
    private String airConsumption;

    private String branchPipeSize;

    @NotBlank
    private String currentConsumption;
}
