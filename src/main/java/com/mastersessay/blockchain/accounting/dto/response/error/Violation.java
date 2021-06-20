package com.mastersessay.blockchain.accounting.dto.response.error;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@Builder
@ToString
public class Violation {
    private final String fieldName;
    private final String message;
}
