package com.mastersessay.blockchain.accounting.model.order;

import com.mastersessay.blockchain.accounting.consts.OrderDevicePurpose;
import com.mastersessay.blockchain.accounting.model.dictionary.facility.AirHandlingUnit;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Entity
@Table(name = "order_air_handling_units")
@Setter(value = AccessLevel.PUBLIC)
@Getter
public class OrderAirHandlingUnit extends RealOrderDevice {
    @ManyToOne
    @JoinColumn(name = "device_id")
    private AirHandlingUnit airHandlingUnit;

    public OrderAirHandlingUnit() {
    }

    @Builder
    public OrderAirHandlingUnit(Integer amount,
                                Order order,
                                @NotNull OrderDevicePurpose orderDevicePurpose,
                                Boolean isOrderCompleted,
                                AirHandlingUnit airHandlingUnit) {
        super(amount, order, orderDevicePurpose, isOrderCompleted);
        this.airHandlingUnit = airHandlingUnit;
    }
}
