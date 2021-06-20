package com.mastersessay.blockchain.accounting.dto.response.order;

import com.mastersessay.blockchain.accounting.consts.OrderDevicePurpose;
import com.mastersessay.blockchain.accounting.dto.response.facility.MiningCoolingResponse;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Data
@Builder
public class OrderMiningCoolingRackResponse implements Serializable {
    private MiningCoolingResponse miningCooling;

    private Integer amount;

    private OrderDevicePurpose orderDevicePurpose;
}
