package com.mastersessay.blockchain.accounting.repository;

import com.mastersessay.blockchain.accounting.model.order.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Optional;

public interface OrderRepository extends PagingAndSortingRepository<Order, Long> {
    Page<Order> getByWaitingActionUsername(String username, Pageable pageable);
}
