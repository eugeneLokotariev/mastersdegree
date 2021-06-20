package com.mastersessay.blockchain.accounting.repository;

import com.mastersessay.blockchain.accounting.model.dictionary.Manufacturer;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ManufacturerRepository extends PagingAndSortingRepository<Manufacturer, Long> {
    Optional<Manufacturer> getById(Long id);

    Optional<Manufacturer> getByName(String name);
}
