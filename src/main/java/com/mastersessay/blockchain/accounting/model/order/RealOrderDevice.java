package com.mastersessay.blockchain.accounting.model.order;

import com.mastersessay.blockchain.accounting.consts.OrderDevicePurpose;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@MappedSuperclass
@Setter(value = AccessLevel.PUBLIC)
@Getter
public abstract class RealOrderDevice implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "device_list_item_id")
    protected Long id;

    @Column(name = "amount")
    protected Integer amount;

    @ManyToOne(optional = false)
    @JoinColumn(name = "order_id")
    protected Order order;

    @Enumerated(EnumType.STRING)
    @Column(name = "order_device_purpose")
    @NotNull
    protected OrderDevicePurpose orderDevicePurpose;

    @Enumerated(EnumType.STRING)
    @Column(name = "previous_order_device_purpose")
    protected OrderDevicePurpose previousOrderDevicePurpose;

    protected Boolean isOrderCompleted;

    public RealOrderDevice() {
    }

    public RealOrderDevice(Integer amount,
                           Order order,
                           @NotNull OrderDevicePurpose orderDevicePurpose,
                           Boolean isOrderCompleted) {
        this.amount = amount;
        this.order = order;
        this.orderDevicePurpose = orderDevicePurpose;
        this.isOrderCompleted = isOrderCompleted;
    }
}
