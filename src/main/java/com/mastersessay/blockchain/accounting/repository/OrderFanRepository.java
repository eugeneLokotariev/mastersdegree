package com.mastersessay.blockchain.accounting.repository;

import com.mastersessay.blockchain.accounting.model.order.OrderAirHandlingUnit;
import com.mastersessay.blockchain.accounting.model.order.OrderFan;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Optional;

public interface OrderFanRepository extends PagingAndSortingRepository<OrderFan, Long> {
}
