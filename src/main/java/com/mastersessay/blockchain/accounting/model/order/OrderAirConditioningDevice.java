package com.mastersessay.blockchain.accounting.model.order;

import com.mastersessay.blockchain.accounting.consts.OrderDevicePurpose;
import com.mastersessay.blockchain.accounting.model.dictionary.facility.AirConditioningDevice;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Entity
@Table(name = "order_air_conditioning_devices")
@Setter(value = AccessLevel.PUBLIC)
@Getter
public class OrderAirConditioningDevice extends RealOrderDevice {
    @ManyToOne
    @JoinColumn(name = "device_id")
    private AirConditioningDevice airConditioningDevice;

    public OrderAirConditioningDevice() {
    }

    @Builder
    public OrderAirConditioningDevice(Integer amount,
                                      Order order,
                                      @NotNull OrderDevicePurpose orderDevicePurpose,
                                      Boolean isOrderCompleted,
                                      AirConditioningDevice airConditioningDevice) {
        super(amount, order, orderDevicePurpose, isOrderCompleted);
        this.airConditioningDevice = airConditioningDevice;
    }
}
