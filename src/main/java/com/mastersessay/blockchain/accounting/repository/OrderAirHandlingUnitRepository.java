package com.mastersessay.blockchain.accounting.repository;

import com.mastersessay.blockchain.accounting.model.order.OrderAirHandlingUnit;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Optional;

public interface OrderAirHandlingUnitRepository extends PagingAndSortingRepository<OrderAirHandlingUnit, Long> {
}
