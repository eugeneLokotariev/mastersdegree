package com.mastersessay.blockchain.accounting.repository;

import com.mastersessay.blockchain.accounting.model.dictionary.facility.MiningFarm;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MiningFarmRepository extends AdditionalSearchableRepository<MiningFarm, Long> {
    Optional<MiningFarm> getById(Long id);

    Optional<MiningFarm> getByModel(String model);

    @Query("select miningFarm " +
            "from MiningFarm miningFarm " +
            "where miningFarm.manufacturer.name = ?1"
    )
    List<MiningFarm> getMiningFarmsByManufacturerName(String manufacturerName);
}
