package com.mastersessay.blockchain.accounting.repository;

import com.mastersessay.blockchain.accounting.model.dictionary.facility.AirConditioningDevice;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;
import java.util.Optional;

public interface AdditionalSearchableRepository<T, Id> extends PagingAndSortingRepository<T, Id> {
    Optional<T> getById(Id id);

    Optional<T> getByModel(String model);

    List<T> getByMatchingName(String name);
}
