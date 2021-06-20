package com.mastersessay.blockchain.accounting.model.dictionary.facility;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@MappedSuperclass
@Setter(value = AccessLevel.PUBLIC)
@Getter
public abstract class AbstractFacility extends UserInfo {
    public AbstractFacility() {
    }

    public AbstractFacility(@NotBlank String createdWhen,
                            @NotBlank String createdBy,
                            String modifiedWhen,
                            String modifiedBy,
                            @NotBlank String model,
                            String power,
                            String noiseLevel,
                            @NotBlank String weight,
                            String size,
                            String voltage,
                            Integer priceUsd,
                            String webReference) {
        super(createdWhen, createdBy, modifiedWhen, modifiedBy);
        this.model = model;
        this.power = power;
        this.noiseLevel = noiseLevel;
        this.weight = weight;
        this.size = size;
        this.voltage = voltage;
        this.priceUsd = priceUsd;
        this.webReference = webReference;
    }

    @Id
    @Column(name = "device_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    @NotBlank
    private String model;

    @Column
    private String power;

    @Column(name = "noise_level")
    private String noiseLevel;

    @Column
    @NotBlank
    private String weight;

    @Column
    private String size;

    @Column
    private String voltage;

    @Column(name = "price_usd")
    private Integer priceUsd;

    @Column(name = "web_reference")
    private String webReference;
}
