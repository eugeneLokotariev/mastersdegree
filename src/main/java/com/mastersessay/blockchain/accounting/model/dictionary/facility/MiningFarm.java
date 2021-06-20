package com.mastersessay.blockchain.accounting.model.dictionary.facility;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.mastersessay.blockchain.accounting.model.dictionary.Manufacturer;
import com.mastersessay.blockchain.accounting.model.order.OrderAirConditioningDevice;
import com.mastersessay.blockchain.accounting.model.order.OrderMiningCoolingRack;
import com.mastersessay.blockchain.accounting.model.order.OrderMiningFarm;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@Entity
@Table(name = "mining_farms",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = "model")
        }
)
@Setter
@Getter
public class MiningFarm extends UserInfo {
    public MiningFarm() {
    }

    @Builder
    public MiningFarm(@NotBlank String createdWhen,
                      @NotBlank String createdBy,
                      String modifiedWhen,
                      String modifiedBy,
                      @NotBlank @Size(max = 256) String model,
                      @Size(max = 1024) String alsoAsKnownAs,
                      String releaseDate,
                      @Size(max = 256) String size,
                      @Size(max = 256) String weight,
                      @Size(max = 256) String noiseLevel,
                      Integer fans,
                      Integer chipCount,
                      String rackFormat,
                      String cooling,
                      @NotBlank @Size(max = 256) String power,
                      @NotBlank @Size(max = 256) String voltage,
                      String interfaceName,
                      @Size(max = 256) String memory,
                      @Size(max = 256) String temperature,
                      @Size(max = 256) String humidity,
                      @NotNull Integer priceUsd, Manufacturer manufacturer) {
        super(createdWhen, createdBy, modifiedWhen, modifiedBy);
        this.model = model;
        this.alsoAsKnownAs = alsoAsKnownAs;
        this.releaseDate = releaseDate;
        this.size = size;
        this.weight = weight;
        this.noiseLevel = noiseLevel;
        this.fans = fans;
        this.chipCount = chipCount;
        this.rackFormat = rackFormat;
        this.cooling = cooling;
        this.power = power;
        this.voltage = voltage;
        this.interfaceName = interfaceName;
        this.memory = memory;
        this.temperature = temperature;
        this.humidity = humidity;
        this.priceUsd = priceUsd;
        this.manufacturer = manufacturer;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "mining_farm_id")
    private Long id;

    @NotBlank
    @Size(max = 256)
    @Column
    private String model;

    @Size(max = 1024)
    @Column(name = "also_as_known_as")
    private String alsoAsKnownAs;

    @Column(name = "release_date")
    private String releaseDate;

    @Size(max = 256)
    @Column
    private String size;

    @Size(max = 256)
    @Column
    private String weight;

    @Size(max = 256)
    @Column(name = "noise_level")
    private String noiseLevel;

    @Column(name = "fans")
    private Integer fans;

    @Column(name = "chip_count")
    private Integer chipCount;

    @Column(name = "rack_format")
    private String rackFormat;

    private String cooling;

    @NotBlank
    @Size(max = 256)
    @Column
    private String power;

    @NotBlank
    @Size(max = 256)
    @Column
    private String voltage;

    @Column(name = "interface")
    private String interfaceName;

    @Size(max = 256)
    @Column
    private String memory;

    @Size(max = 256)
    @Column
    private String temperature;

    @Size(max = 256)
    @Column
    private String humidity;

    @Column(name = "price_usd")
    @NotNull
    private Integer priceUsd;

    @ManyToOne(optional = false)
    @JoinColumn(name = "manufacturer_id")
    private Manufacturer manufacturer;

    @OneToMany(
            fetch = FetchType.LAZY,
            mappedBy = "miningFarm",
            orphanRemoval = true,
            cascade = CascadeType.ALL
    )
    @JsonBackReference
    private List<OrderMiningFarm> orderMiningFarms;
}
