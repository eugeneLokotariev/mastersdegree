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
@Builder(toBuilder = true)
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@NoArgsConstructor(access = AccessLevel.PACKAGE)
@Setter(value = AccessLevel.PUBLIC)
@Getter
public class OrderFan implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "device_list_item_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "device_id")
    private Fan fan;

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
