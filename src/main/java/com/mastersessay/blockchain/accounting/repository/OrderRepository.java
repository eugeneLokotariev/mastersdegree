package com.mastersessay.blockchain.accounting.repository;

import com.mastersessay.blockchain.accounting.consts.OrderStatus;
import com.mastersessay.blockchain.accounting.consts.OrderType;
import com.mastersessay.blockchain.accounting.model.order.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface OrderRepository extends PagingAndSortingRepository<Order, Long> {
    Page<Order> getByWaitingActionUsername(String username, Pageable pageable);

    @Query("select ordr " +
            "from Order as ordr " +
            "where ordr.orderType = :orderType " +
            "and ordr.status = :status ")
    List<Order> getOrdersByOrderTypeAndStatus(OrderType orderType, OrderStatus status);

    @Query("select ordr " +
            "from Order as ordr " +
            "where upper(ordr.name) like %:name% ")
    List<Order> getByMatchingName(String name);
}
