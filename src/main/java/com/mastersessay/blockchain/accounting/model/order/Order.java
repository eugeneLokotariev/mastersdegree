package com.mastersessay.blockchain.accounting.model.order;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.mastersessay.blockchain.accounting.consts.OrderStatus;
import com.mastersessay.blockchain.accounting.consts.OrderType;
import com.mastersessay.blockchain.accounting.model.dictionary.facility.*;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Entity(name = "Order")
@Table(name = "orders")
@Setter
@Getter
public class Order extends UserInfo {
    public Order() {
    }

    @Builder
    public Order(@NotBlank String createdWhen,
                 @NotBlank String createdBy,
                 String modifiedWhen,
                 String modifiedBy,
                 @NotNull OrderStatus status,
                 @NotNull OrderType orderType,
                 String name,
                 String waitingActionUsername,
                 List<OrdersActionHistory> ordersActionHistoryItems,
                 List<OrderMiningFarm> orderMiningFarms,
                 List<OrderMiningCoolingRack> orderMiningCoolingRacks,
                 List<OrderFan> orderFans,
                 List<OrderAirHandlingUnit> orderAirHandlingUnits,
                 List<OrderAirConditioningDevice> orderAirConditioningDevices) {
        super(createdWhen, createdBy, modifiedWhen, modifiedBy);
        this.status = status;
        this.orderType = orderType;
        this.name = name;
        this.ordersActionHistoryItems = ordersActionHistoryItems;
        this.waitingActionUsername = waitingActionUsername;
        this.orderMiningFarms = orderMiningFarms;
        this.orderMiningCoolingRacks = orderMiningCoolingRacks;
        this.orderFans = orderFans;
        this.orderAirHandlingUnits = orderAirHandlingUnits;
        this.orderAirConditioningDevices = orderAirConditioningDevices;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id")
    private Long orderId;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    @NotNull
    private OrderStatus status;

    @Enumerated(EnumType.STRING)
    @Column(name = "order_type")
    @NotNull
    private OrderType orderType;

    @Column(name = "name")
    private String name;

    @Column(name = "waiting_action_username")
    private String waitingActionUsername;

    @OneToMany(
            fetch = FetchType.LAZY,
            mappedBy = "order",
            orphanRemoval = true,
            cascade = CascadeType.ALL
    )
    @JsonBackReference
    private List<OrdersActionHistory> ordersActionHistoryItems;

    @OneToMany(
            fetch = FetchType.LAZY,
            mappedBy = "order",
            orphanRemoval = true,
            cascade = CascadeType.ALL
    )
    @JsonBackReference
    private List<OrderMiningFarm> orderMiningFarms;

    @OneToMany(
            fetch = FetchType.LAZY,
            mappedBy = "order",
            orphanRemoval = true,
            cascade = CascadeType.ALL
    )
    @JsonBackReference
    private List<OrderMiningCoolingRack> orderMiningCoolingRacks;

    @OneToMany(
            fetch = FetchType.LAZY,
            mappedBy = "order",
            orphanRemoval = true,
            cascade = CascadeType.ALL
    )
    @JsonBackReference
    private List<OrderFan> orderFans;

    @OneToMany(
            fetch = FetchType.LAZY,
            mappedBy = "order",
            orphanRemoval = true,
            cascade = CascadeType.ALL
    )
    @JsonBackReference
    private List<OrderAirHandlingUnit> orderAirHandlingUnits;

    @OneToMany(
            fetch = FetchType.LAZY,
            mappedBy = "order",
            orphanRemoval = true,
            cascade = CascadeType.ALL
    )
    @JsonBackReference
    private List<OrderAirConditioningDevice> orderAirConditioningDevices;
}
