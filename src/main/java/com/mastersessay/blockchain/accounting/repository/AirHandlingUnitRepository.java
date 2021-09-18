package com.mastersessay.blockchain.accounting.repository;

import com.mastersessay.blockchain.accounting.model.dictionary.facility.AirHandlingUnit;
import com.mastersessay.blockchain.accounting.model.dictionary.facility.Fan;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AirHandlingUnitRepository extends AdditionalSearchableRepository<AirHandlingUnit, Long> {
    Optional<AirHandlingUnit> getById(Long id);

    Optional<AirHandlingUnit> getByModel(String model);

    @Query("select airHandlingUnit " +
            "from AirHandlingUnit airHandlingUnit " +
            "where airHandlingUnit.manufacturer.name = ?1"
    )
    List<AirHandlingUnit> getAirHandlingUnitsByManufacturerName(String manufacturerName);

    @Query("select airHandlingUnit " +
            "from AirHandlingUnit airHandlingUnit " +
            "where upper(airHandlingUnit.model) like %:name%"
    )
    List<AirHandlingUnit> getByMatchingName(String name);
}
