package com.mastersessay.blockchain.accounting.repository;

import com.mastersessay.blockchain.accounting.model.order.OrdersActionHistory;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface OrdersActionHistoryRepository extends PagingAndSortingRepository<OrdersActionHistory, Long> {
}
