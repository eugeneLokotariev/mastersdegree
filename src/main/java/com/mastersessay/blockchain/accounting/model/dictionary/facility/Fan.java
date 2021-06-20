package com.mastersessay.blockchain.accounting.model.dictionary.facility;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.mastersessay.blockchain.accounting.model.dictionary.Manufacturer;
import com.mastersessay.blockchain.accounting.model.order.OrderAirConditioningDevice;
import com.mastersessay.blockchain.accounting.model.order.OrderAirHandlingUnit;
import com.mastersessay.blockchain.accounting.model.order.OrderFan;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Entity
@Table(name = "fans")
@Setter
@Getter
public class Fan extends AbstractFacility {
    public Fan() {
    }

    @Builder
    public Fan(@NotBlank String createdWhen,
               @NotBlank String createdBy,
               String modifiedWhen,
               String modifiedBy,
               @NotBlank String model,
               @NotBlank String power,
               String noiseLevel,
               @NotBlank String weight,
               String size,
               @NotBlank String voltage,
               @NotNull Integer priceUsd,
               @NotBlank String airConsumption,
               String branchPipeSize,
               @NotBlank String currentConsumption,
               Manufacturer manufacturer,
               String webReference) {
        super(createdWhen, createdBy, modifiedWhen, modifiedBy, model, power, noiseLevel, weight, size, voltage, priceUsd, webReference);
        this.airConsumption = airConsumption;
        this.branchPipeSize = branchPipeSize;
        this.currentConsumption = currentConsumption;
        this.manufacturer = manufacturer;
    }

    @NotBlank
    @Column(name = "air_consumption")
    private String airConsumption;

    @Column(name = "branch_pipe_size")
    private String branchPipeSize;

    @NotBlank
    @Column(name = "current_consumption")
    private String currentConsumption;

    @ManyToOne(optional = false)
    @JoinColumn(name = "manufacturer_id")
    private Manufacturer manufacturer;

    @OneToMany(
            fetch = FetchType.LAZY,
            mappedBy = "fan",
            orphanRemoval = true,
            cascade = CascadeType.ALL
    )
    @JsonBackReference
    private List<OrderFan> orderFans;
}
