package com.mastersessay.blockchain.accounting.consts;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import static com.mastersessay.blockchain.accounting.consts.BlockchainAccountingConstants.BusinessMessages.ENUM_VALUE_NOT_FOUND;

public enum OrderStatus {
    EMPTY (""),
    PLANNED("Planned"),
    IN_PROGRESS("In progress"),
    SUSPENDED("Suspended"),
    CANCELLED("Cancelled"),
    COMPLETED("Completed"),
    WAITING_FOR_ACTION("Waiting for action");

    private String statusInText;

    OrderStatus(String statusInText) {
        this.statusInText = statusInText;
    }

    public String getStatusInText() {
        return statusInText;
    }

    @Override
    @JsonValue
    public String toString() {
        return statusInText;
    }

    @JsonCreator
    public static OrderStatus fromTextName(String inputName) {
        for (OrderStatus orderStatus : OrderStatus.values()) {
            if (orderStatus.statusInText.equalsIgnoreCase(inputName)) {
                return orderStatus;
            }
        }

        throw new IllegalArgumentException(ENUM_VALUE_NOT_FOUND);
    }
}
