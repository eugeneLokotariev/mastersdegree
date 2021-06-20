package com.mastersessay.blockchain.accounting.dto.response.order;

import com.mastersessay.blockchain.accounting.consts.OrderStatus;
import com.mastersessay.blockchain.accounting.consts.OrderType;
import com.mastersessay.blockchain.accounting.dto.request.order.*;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

@Data
@Builder
public class OrderResponse implements Serializable {
    private Long orderId;

    @NotNull
    private OrderStatus status;

    @NotNull
    private OrderType orderType;

    private String name;

    private String waitingActionUsername;

    private List<OrderActionHistoryDto> orderActionHistory;

    private List<OrderMiningFarmResponse> orderMiningFarms;

    private List<OrderMiningCoolingRackResponse> orderMiningCoolingRacks;

    private List<OrderAirConditioningDeviceResponse> orderAirConditioningDevices;

    private List<OrderAirHandlingUnitResponse> orderAirHandlingUnits;

    private List<OrderFanResponse> orderFanDs;
}
