package com.mastersessay.blockchain.accounting.model.dictionary.facility;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.mastersessay.blockchain.accounting.model.dictionary.Manufacturer;
import com.mastersessay.blockchain.accounting.model.order.OrderAirConditioningDevice;
import com.mastersessay.blockchain.accounting.model.order.OrderMiningCoolingRack;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Entity
@Table(name = "mining_cooling_racks")
@Setter
@Getter
public class MiningCoolingRack extends AbstractFacility {
    public MiningCoolingRack() {
    }

    @Builder
    public MiningCoolingRack(@NotBlank String createdWhen,
                             @NotBlank String createdBy,
                             String modifiedWhen,
                             String modifiedBy,
                             @NotBlank String model,
                             @NotBlank String power,
                             String noiseLevel,
                             @NotBlank String weight,
                             @NotBlank String size,
                             @NotBlank String voltage,
                             @NotNull Integer priceUsd,
                             @NotNull Integer waterCapacity,
                             @NotBlank String optimalWaterConsumption,
                             @NotBlank String maxCoolingPower,
                             @NotBlank String pumpConsumption,
                             Manufacturer manufacturer,
                             String webReference) {
        super(createdWhen, createdBy, modifiedWhen, modifiedBy, model, power, noiseLevel, weight, size, voltage, priceUsd, webReference);
        this.waterCapacity = waterCapacity;
        this.optimalWaterConsumption = optimalWaterConsumption;
        this.maxCoolingPower = maxCoolingPower;
        this.pumpConsumption = pumpConsumption;
        this.manufacturer = manufacturer;
    }

    @NotNull
    @Column(name = "water_capacity")
    private Integer waterCapacity;

    @NotBlank
    @Column(name = "optimal_water_consumption")
    private String optimalWaterConsumption;

    @NotBlank
    @Column(name = "max_cooling_power")
    private String maxCoolingPower;

    @NotBlank
    @Column(name = "pump_consumption")
    private String pumpConsumption;

    @ManyToOne(optional = false)
    @JoinColumn(name = "manufacturer_id")
    @JsonManagedReference
    private Manufacturer manufacturer;

    @OneToMany(
            fetch = FetchType.LAZY,
            mappedBy = "miningCoolingRack",
            orphanRemoval = true,
            cascade = CascadeType.ALL
    )
    @JsonBackReference
    private List<OrderMiningCoolingRack> orderMiningCoolingRacks;
}
