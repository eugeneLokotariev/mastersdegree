package com.mastersessay.blockchain.accounting.dto.response.order;

import com.mastersessay.blockchain.accounting.consts.OrderDevicePurpose;
import com.mastersessay.blockchain.accounting.dto.response.facility.FanResponse;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Data
@Builder
public class OrderFanResponse implements Serializable {
    private FanResponse fan;

    private Integer amount;

    private OrderDevicePurpose orderDevicePurpose;

    private OrderDevicePurpose previousOrderDevicePurpose;
}
