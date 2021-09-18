package com.mastersessay.blockchain.accounting.repository;

import com.mastersessay.blockchain.accounting.model.dictionary.facility.AirConditioningDevice;
import com.mastersessay.blockchain.accounting.model.dictionary.facility.MiningCoolingRack;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AirConditioningDeviceRepository extends AdditionalSearchableRepository<AirConditioningDevice, Long> {
    Optional<AirConditioningDevice> getById(Long id);

    Optional<AirConditioningDevice> getByModel(String model);

    @Query("select airCondioningDevice " +
            "from AirConditioningDevice airCondioningDevice " +
            "where airCondioningDevice.manufacturer.name = ?1"
    )
    List<AirConditioningDevice> getAirConditioningDevicesByManufacturerName(String manufacturerName);

    @Query("select device " +
            "from AirConditioningDevice device " +
            "where upper(device.model) like %:name%"
    )
    List<AirConditioningDevice> getByMatchingName(String name);
}
