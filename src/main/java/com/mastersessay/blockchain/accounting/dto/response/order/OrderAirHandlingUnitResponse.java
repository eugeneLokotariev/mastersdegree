package com.mastersessay.blockchain.accounting.dto.response.order;

import com.mastersessay.blockchain.accounting.consts.OrderDevicePurpose;
import com.mastersessay.blockchain.accounting.dto.response.facility.AirHandlingUnitResponse;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Builder
@Data
public class OrderAirHandlingUnitResponse implements Serializable {
    private AirHandlingUnitResponse airHandlingUnit;

    private Integer amount;

    private OrderDevicePurpose orderDevicePurpose;
}
