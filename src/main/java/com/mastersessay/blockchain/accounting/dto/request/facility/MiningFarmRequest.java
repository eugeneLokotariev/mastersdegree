package com.mastersessay.blockchain.accounting.dto.request.facility;

import lombok.Builder;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.YearMonth;

@Data
@Builder
public class MiningFarmRequest implements Serializable {
    @NotBlank
    private String model;

    @Size(max = 1024)
    private String alsoAsKnownAs;

    @DateTimeFormat(pattern = "yyyy-MM")
    private YearMonth releaseDate;

    @Size(max = 256)
    private String size;

    @Size(max = 256)
    private String weight;

    @Size(max = 256)
    private String noiseLevel;

    private Integer fans;

    private Integer chipCount;

    private String rackFormat;

    private String cooling;

    @NotBlank
    @Size(max = 256)
    private String power;

    @NotBlank
    @Size(max = 256)
    private String voltage;

    private String interfaceName;

    @Size(max = 256)
    private String memory;

    @Size(max = 256)
    private String temperature;

    @Size(max = 256)
    private String humidity;

    @NotNull
    private Integer priceUsd;

    @NotNull
    private ManufacturerRequest manufacturer;
}
