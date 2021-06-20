package com.mastersessay.blockchain.accounting.model.dictionary.facility;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.mastersessay.blockchain.accounting.consts.AirConditioningDeviceType;
import com.mastersessay.blockchain.accounting.model.dictionary.Manufacturer;
import com.mastersessay.blockchain.accounting.model.order.OrderAirConditioningDevice;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Entity
@Table(name = "air_conditioning_devices")
@Setter
@Getter
public class AirConditioningDevice extends AbstractFacility {
    public AirConditioningDevice() {
    }

    @Builder
    public AirConditioningDevice(@NotBlank String createdWhen,
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
                                 AirConditioningDeviceType airConditioningDeviceType,
                                 @NotBlank String coolingCapacity,
                                 @NotNull Integer roomAreaSquareM,
                                 Manufacturer manufacturer,
                                 String webReference) {
        super(createdWhen, createdBy, modifiedWhen, modifiedBy, model, power, noiseLevel, weight, size, voltage, priceUsd, webReference);
        this.airConditioningDeviceType = airConditioningDeviceType;
        this.coolingCapacity = coolingCapacity;
        this.roomAreaSquareM = roomAreaSquareM;
        this.manufacturer = manufacturer;
    }

    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    @NotNull
    private AirConditioningDeviceType airConditioningDeviceType;

    @NotBlank
    @Column(name = "cooling_capacity")
    private String coolingCapacity;

    @NotNull
    @Column(name = "room_area_squareM")
    private Integer roomAreaSquareM;

    @ManyToOne(optional = false)
    @JoinColumn(name = "manufacturer_id")
    private Manufacturer manufacturer;

    @OneToMany(
            fetch = FetchType.LAZY,
            mappedBy = "airConditioningDevice",
            orphanRemoval = true,
            cascade = CascadeType.ALL
    )
    @JsonBackReference
    private List<OrderAirConditioningDevice> orderAirConditioningDevices;
}
