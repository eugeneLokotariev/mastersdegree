package com.mastersessay.blockchain.accounting.dto.response.facility;

import com.mastersessay.blockchain.accounting.dto.request.facility.ManufacturerRequest;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
@Builder
public class FanResponse implements Serializable {
    private Long id;

    private String model;

    private String power;

    private String noiseLevel;

    private String weight;

    private String size;

    private String voltage;

    private Integer priceUsd;

    private String webReference;

    @NotNull
    private ManufacturerResponse manufacturer;

    @NotBlank
    private String airConsumption;

    private String branchPipeSize;

    @NotBlank
    private String currentConsumption;

    private String createdWhen;

    private String createdBy;

    private String modifiedWhen;

    private String modifiedBy;
}
