package com.mastersessay.blockchain.accounting.model.order;

import com.mastersessay.blockchain.accounting.consts.OrderDevicePurpose;
import com.mastersessay.blockchain.accounting.model.dictionary.facility.AirConditioningDevice;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Entity
@Table(name = "order_air_conditioning_devices")
@Builder(toBuilder = true)
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@NoArgsConstructor(access = AccessLevel.PACKAGE)
@Setter(value = AccessLevel.PUBLIC)
@Getter
public class OrderAirConditioningDevice implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "device_list_item_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "device_id")
    private AirConditioningDevice airConditioningDevice;

    @Column(name = "amount")
    private Integer amount;

    @ManyToOne(optional = false)
    @JoinColumn(name = "order_id")
    private Order order;

    @Enumerated(EnumType.STRING)
    @Column(name = "order_device_purpose")
    @NotNull
    private OrderDevicePurpose orderDevicePurpose;
}
