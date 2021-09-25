package com.mastersessay.blockchain.accounting.model.order;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.mastersessay.blockchain.accounting.consts.OrderDevicePurpose;
import com.mastersessay.blockchain.accounting.model.dictionary.facility.AirConditioningDevice;
import com.mastersessay.blockchain.accounting.model.dictionary.facility.MiningCoolingRack;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "order_mining_cooling_racks")
@Setter(value = AccessLevel.PUBLIC)
@Getter
public class OrderMiningCoolingRack extends RealOrderDevice {
    @ManyToOne
    @JoinColumn(name = "device_id")
    private MiningCoolingRack miningCoolingRack;

    public OrderMiningCoolingRack() {
    }

    @Builder
    public OrderMiningCoolingRack(Integer amount,
                                  Order order,
                                  @NotNull OrderDevicePurpose orderDevicePurpose,
                                  Boolean isOrderCompleted,
                                  MiningCoolingRack miningCoolingRack) {
        super(amount, order, orderDevicePurpose, isOrderCompleted);
        this.miningCoolingRack = miningCoolingRack;
    }
}
