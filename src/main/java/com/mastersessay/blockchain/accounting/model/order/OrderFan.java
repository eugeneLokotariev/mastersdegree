package com.mastersessay.blockchain.accounting.model.order;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.mastersessay.blockchain.accounting.consts.OrderDevicePurpose;
import com.mastersessay.blockchain.accounting.model.dictionary.facility.AirConditioningDevice;
import com.mastersessay.blockchain.accounting.model.dictionary.facility.AirHandlingUnit;
import com.mastersessay.blockchain.accounting.model.dictionary.facility.Fan;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "order_fans")
@Setter(value = AccessLevel.PUBLIC)
@Getter
public class OrderFan extends RealOrderDevice {
    @ManyToOne
    @JoinColumn(name = "device_id")
    private Fan fan;

    public OrderFan() {
    }

    @Builder
    public OrderFan(Integer amount,
                    Order order,
                    @NotNull OrderDevicePurpose orderDevicePurpose,
                    Boolean isOrderCompleted,
                    Fan fan) {
        super(amount, order, orderDevicePurpose, isOrderCompleted);
        this.fan = fan;
    }
}
