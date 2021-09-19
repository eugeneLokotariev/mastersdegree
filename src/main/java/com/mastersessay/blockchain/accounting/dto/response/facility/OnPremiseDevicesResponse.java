package com.mastersessay.blockchain.accounting.dto.response.facility;

import com.mastersessay.blockchain.accounting.dto.request.order.OrderActionHistoryDto;
import com.mastersessay.blockchain.accounting.dto.response.order.*;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class OnPremiseDevicesResponse {
    private List<OrderMiningFarmResponse> onPremiseMiningFarms;

    private List<OrderMiningCoolingRackResponse> onPremiseMiningCoolingRacks;

    private List<OrderAirConditioningDeviceResponse> onPremiseAirConditioningDevices;

    private List<OrderAirHandlingUnitResponse> onPremiseAirHandlingUnits;

    private List<OrderFanResponse> onPremiseFans;
}
