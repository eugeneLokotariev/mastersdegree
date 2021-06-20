package com.mastersessay.blockchain.accounting.repository;

import com.mastersessay.blockchain.accounting.model.order.OrderMiningCoolingRack;
import com.mastersessay.blockchain.accounting.model.order.OrderMiningFarm;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Optional;

public interface OrderMiningFarmRepository extends PagingAndSortingRepository<OrderMiningFarm, Long> {
}
