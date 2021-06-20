package com.mastersessay.blockchain.accounting.repository;

import com.mastersessay.blockchain.accounting.model.dictionary.facility.AirConditioningDevice;
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
}
