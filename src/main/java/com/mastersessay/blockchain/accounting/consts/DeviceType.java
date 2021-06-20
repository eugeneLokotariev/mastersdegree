package com.mastersessay.blockchain.accounting.consts;

import com.fasterxml.jackson.annotation.JsonCreator;

import static com.mastersessay.blockchain.accounting.consts.BlockchainAccountingConstants.BusinessMessages.ENUM_VALUE_NOT_FOUND;

public enum DeviceType {
    AIR_CONDITIONING_DEVICE("airConditioningDevice"),
    AIR_HANDLING_UNIT("airHandlingUnit"),
    FAN("fan"),
    MINING_COOLING_RACK("miningCoolingRack"),
    MINING_FARM("miningFarm");

    private String apiName;

    DeviceType(String name) {
        this.apiName = name;
    }

    public String getApiName() {
        return apiName;
    }

    @Override
    public String toString() {
        return apiName;
    }

    @JsonCreator
    public static DeviceType fromTextName(String inputName) {
        for (DeviceType deviceType : DeviceType.values()) {
            if (deviceType.apiName.equalsIgnoreCase(inputName)) {
                return deviceType;
            }
        }

        throw new IllegalArgumentException(ENUM_VALUE_NOT_FOUND);
    }
}
