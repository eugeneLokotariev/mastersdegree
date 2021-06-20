package com.mastersessay.blockchain.accounting.repository;

import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Optional;

public interface AdditionalSearchableRepository<T, Id> extends PagingAndSortingRepository<T, Id> {
    Optional<T> getById(Id id);

    Optional<T> getByModel(String model);
}
