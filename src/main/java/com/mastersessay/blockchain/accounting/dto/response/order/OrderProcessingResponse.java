package com.mastersessay.blockchain.accounting.dto.response.order;

import com.mastersessay.blockchain.accounting.consts.OrderStatus;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Data
@Builder
public class OrderProcessingResponse implements Serializable {
    private OrderResponse orderResponse;

    private String actionExecutingDate;

    private String actionExecutionUsername;

    private OrderStatus statusFrom;

    private OrderStatus statusTo;

    private String actionComment;
}
