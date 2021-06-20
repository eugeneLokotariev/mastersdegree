package com.mastersessay.blockchain.accounting.dto.request.order;

import com.mastersessay.blockchain.accounting.consts.OrderStatus;
import com.mastersessay.blockchain.accounting.consts.OrderType;
import lombok.*;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

@Data
@Builder
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@NoArgsConstructor(access = AccessLevel.PACKAGE)
public class OrderRequest implements Serializable {
    @NotNull
    private OrderStatus status;

    @NotNull
    private OrderType orderType;

    private String waitingActionUsername;

    private String actionComment;

    private List<OrderMiningFarmRequest> orderMiningFarms;

    private List<OrderMiningCoolingRackRequest> orderMiningCoolingRacks;

    private List<OrderAirConditioningDeviceRequest> orderAirConditioningDevices;

    private List<OrderAirHandlingUnitRequest> orderAirHandlingUnits;

    private List<OrderFanRequest> orderFanDs;
}
