package com.mastersessay.blockchain.accounting.dto.response.order;

import com.mastersessay.blockchain.accounting.consts.OrderDevicePurpose;
import com.mastersessay.blockchain.accounting.dto.response.facility.MiningFarmResponse;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Data
@Builder
public class OrderMiningFarmResponse implements Serializable {
    private MiningFarmResponse miningFarm;

    private Integer amount;

    private OrderDevicePurpose orderDevicePurpose;
}
