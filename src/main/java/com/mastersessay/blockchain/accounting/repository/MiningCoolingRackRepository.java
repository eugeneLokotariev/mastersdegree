package com.mastersessay.blockchain.accounting.repository;

import com.mastersessay.blockchain.accounting.model.dictionary.facility.Fan;
import com.mastersessay.blockchain.accounting.model.dictionary.facility.MiningCoolingRack;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MiningCoolingRackRepository extends AdditionalSearchableRepository<MiningCoolingRack, Long> {
    Optional<MiningCoolingRack> getById(Long id);

    Optional<MiningCoolingRack> getByModel(String model);

    @Query("select rack " +
            "from MiningCoolingRack rack " +
            "where rack.manufacturer.name = ?1"
    )
    List<MiningCoolingRack> getMiningCoolingRacksByManufacturerName(String manufacturerName);

    @Query("select miningCoolingRack " +
            "from MiningCoolingRack miningCoolingRack " +
            "where upper(miningCoolingRack.model) like %:name%"
    )
    List<MiningCoolingRack> getByMatchingName(String name);
}
