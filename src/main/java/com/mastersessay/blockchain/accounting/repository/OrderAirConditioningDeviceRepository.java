package com.mastersessay.blockchain.accounting.repository;

import com.mastersessay.blockchain.accounting.model.order.OrderAirConditioningDevice;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Optional;

public interface OrderAirConditioningDeviceRepository extends PagingAndSortingRepository<OrderAirConditioningDevice, Long> {
}
