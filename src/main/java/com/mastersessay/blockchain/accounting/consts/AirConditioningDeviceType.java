package com.mastersessay.blockchain.accounting.consts;

import com.fasterxml.jackson.annotation.JsonCreator;

import static com.mastersessay.blockchain.accounting.consts.BlockchainAccountingConstants.BusinessMessages.ENUM_VALUE_NOT_FOUND;

public enum AirConditioningDeviceType {
    SPLIT_SYSTEM("Split"),
    CANAL("Canal");

    private String name;

    AirConditioningDeviceType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return name;
    }

    @JsonCreator
    public static AirConditioningDeviceType fromTextName(String inputName) {
        for (AirConditioningDeviceType airConditioningDeviceType : AirConditioningDeviceType.values()) {
            if (airConditioningDeviceType.name.equalsIgnoreCase(inputName)) {
                return airConditioningDeviceType;
            }
        }

        throw new IllegalArgumentException(ENUM_VALUE_NOT_FOUND);
    }
}
