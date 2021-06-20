package com.mastersessay.blockchain.accounting.model.dictionary;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.mastersessay.blockchain.accounting.model.dictionary.facility.*;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.List;

@Entity
@Table(name = "manufacturers",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = "name")
        }
)
@Setter
@Getter
public class Manufacturer extends UserInfo {
    public Manufacturer() {
    }

    @Builder
    public Manufacturer(@NotBlank String createdWhen,
                        @NotBlank String createdBy,
                        String modifiedWhen,
                        String modifiedBy,
                        @NotBlank @Size(max = 1024) String name,
                        List<MiningFarm> miningFarms,
                        List<MiningCoolingRack> miningCoolingRacks,
                        List<Fan> fans,
                        List<AirHandlingUnit> airHandlingUnits,
                        List<AirConditioningDevice> airConditioningDevices) {
        super(createdWhen, createdBy, modifiedWhen, modifiedBy);
        this.name = name;
        this.miningFarms = miningFarms;
        this.miningCoolingRacks = miningCoolingRacks;
        this.fans = fans;
        this.airHandlingUnits = airHandlingUnits;
        this.airConditioningDevices = airConditioningDevices;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "manufacturer_id")
    private Long id;

    @NotBlank
    @Size(max = 1024)
    @Column
    private String name;

    @OneToMany(
            fetch = FetchType.LAZY,
            mappedBy = "manufacturer",
            orphanRemoval = true,
            cascade = CascadeType.ALL
    )
    @JsonBackReference
    private List<MiningFarm> miningFarms;

    @OneToMany(
            fetch = FetchType.LAZY,
            mappedBy = "manufacturer",
            orphanRemoval = true,
            cascade = CascadeType.ALL
    )
    @JsonBackReference
    private List<MiningCoolingRack> miningCoolingRacks;

    @OneToMany(
            fetch = FetchType.LAZY,
            mappedBy = "manufacturer",
            orphanRemoval = true,
            cascade = CascadeType.ALL
    )
    @JsonBackReference
    private List<Fan> fans;

    @OneToMany(
            fetch = FetchType.LAZY,
            mappedBy = "manufacturer",
            orphanRemoval = true,
            cascade = CascadeType.ALL
    )
    @JsonBackReference
    private List<AirHandlingUnit> airHandlingUnits;

    @OneToMany(
            fetch = FetchType.LAZY,
            mappedBy = "manufacturer",
            orphanRemoval = true,
            cascade = CascadeType.ALL
    )
    @JsonBackReference
    private List<AirConditioningDevice> airConditioningDevices;
}
