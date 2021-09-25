package com.mastersessay.blockchain.accounting.model.order;

import com.mastersessay.blockchain.accounting.consts.OrderDevicePurpose;
import com.mastersessay.blockchain.accounting.model.dictionary.facility.MiningFarm;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Entity
@Table(name = "order_mining_farms")
@Setter(value = AccessLevel.PUBLIC)
@Getter
public class OrderMiningFarm extends RealOrderDevice {
    @ManyToOne
    @JoinColumn(name = "mining_farm_id")
    private MiningFarm miningFarm;

    public OrderMiningFarm() {
    }

    @Builder
    public OrderMiningFarm(Integer amount,
                           Order order,
                           @NotNull OrderDevicePurpose orderDevicePurpose,
                           Boolean isOrderCompleted,
                           MiningFarm miningFarm) {
        super(amount, order, orderDevicePurpose, isOrderCompleted);
        this.miningFarm = miningFarm;
    }
}
