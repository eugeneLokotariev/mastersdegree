package com.mastersessay.blockchain.accounting.model.dictionary.facility;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.mastersessay.blockchain.accounting.model.dictionary.Manufacturer;
import com.mastersessay.blockchain.accounting.model.order.OrderAirHandlingUnit;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.List;

@Entity
@Table(name = "air_handling_units")
@Setter
@Getter
public class AirHandlingUnit extends AbstractFacility {
    public AirHandlingUnit() {
    }

    @Builder
    public AirHandlingUnit(@NotBlank String createdWhen,
                           @NotBlank String createdBy,
                           String modifiedWhen,
                           String modifiedBy,
                           @NotBlank String model,
                           @NotBlank String power,
                           String noiseLevel,
                           @NotBlank String weight,
                           @NotBlank String size,
                           @NotBlank String voltage,
                           Integer priceUsd,
                           @NotBlank String pipeDiameter,
                           @NotBlank String ventilatedArea,
                           @NotBlank String performance,
                           Manufacturer manufacturer,
                           String webReference) {
        super(createdWhen, createdBy, modifiedWhen, modifiedBy, model, power, noiseLevel, weight, size, voltage, priceUsd, webReference);
        this.pipeDiameter = pipeDiameter;
        this.ventilatedArea = ventilatedArea;
        this.performance = performance;
        this.manufacturer = manufacturer;
    }

    @NotBlank
    @Column(name = "pipe_diameter")
    private String pipeDiameter;

    @NotBlank
    @Column(name = "ventilated_area")
    private String ventilatedArea;

    @NotBlank
    @Column
    private String performance;

    @ManyToOne(optional = false)
    @JoinColumn(name = "manufacturer_id")
    private Manufacturer manufacturer;

    @OneToMany(
            fetch = FetchType.LAZY,
            mappedBy = "airHandlingUnit",
            orphanRemoval = true,
            cascade = CascadeType.ALL
    )
    @JsonBackReference
    private List<OrderAirHandlingUnit> orderAirHandlingUnits;
}
