package com.mastersessay.blockchain.accounting.repository;

import com.mastersessay.blockchain.accounting.model.dictionary.Manufacturer;
import com.mastersessay.blockchain.accounting.model.dictionary.facility.Fan;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FanRepository extends AdditionalSearchableRepository<Fan, Long> {
    Optional<Fan> getById(Long id);

    Optional<Fan> getByModel(String model);

    @Query("select fan " +
            "from Fan fan " +
            "where upper(fan.model) like %:name%"
    )
    List<Fan> getByMatchingName(String name);
}
