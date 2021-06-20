package com.mastersessay.blockchain.accounting.dto.request.order;

import com.mastersessay.blockchain.accounting.consts.OrderStatus;
import lombok.*;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
@Builder
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@NoArgsConstructor(access = AccessLevel.PACKAGE)
public class OrderActionHistoryDto implements Serializable {
    private Long id;

    @NotNull
    private String actionExecutingDate;

    @NotNull
    private String actionExecutionUsername;

    private OrderStatus statusFrom;

    private OrderStatus statusTo;

    private String actionComment;
}
