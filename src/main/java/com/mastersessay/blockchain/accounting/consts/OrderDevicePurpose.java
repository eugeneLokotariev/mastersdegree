package com.mastersessay.blockchain.accounting.consts;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import static com.mastersessay.blockchain.accounting.consts.BlockchainAccountingConstants.BusinessMessages.ENUM_VALUE_NOT_FOUND;

public enum OrderDevicePurpose {
    PURCHASE("Purchase"),
    MAINTENANCE("Maintenance"),
    INSTALLATION("Installation"),
    RECONFIGURATION("Reconfiguration"),
    REPLACING("Replacing");

    private String orderTypeInString;

    OrderDevicePurpose(String orderTypeInString) {
        this.orderTypeInString = orderTypeInString;
    }

    public String getOrderTypeInString() {
        return orderTypeInString;
    }

    @Override
    @JsonValue
    public String toString() {
        return orderTypeInString;
    }

    @JsonCreator
    public static OrderDevicePurpose fromTextName(String inputName) {
        for (OrderDevicePurpose orderType : OrderDevicePurpose.values()) {
            if (orderType.orderTypeInString.equalsIgnoreCase(inputName)) {
                return orderType;
            }
        }

        throw new IllegalArgumentException(ENUM_VALUE_NOT_FOUND);
    }
}
