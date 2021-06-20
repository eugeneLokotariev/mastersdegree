package com.mastersessay.blockchain.accounting.repository;

import com.mastersessay.blockchain.accounting.model.order.OrderMiningCoolingRack;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Optional;

public interface OrderMiningCoolingRackRepository extends PagingAndSortingRepository<OrderMiningCoolingRack, Long> {
}
