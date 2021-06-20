package com.mastersessay.blockchain.accounting.dto.request.order;

import com.mastersessay.blockchain.accounting.consts.OrderDevicePurpose;
import lombok.*;

import java.io.Serializable;

@Data
@Builder
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@NoArgsConstructor(access = AccessLevel.PACKAGE)
public class OrderAirConditioningDeviceRequest implements Serializable {
    private Long airConditioningDeviceId;

    private Integer amount;

    private OrderDevicePurpose orderDevicePurpose;

    private Boolean toDeleteFromOrder;
}
