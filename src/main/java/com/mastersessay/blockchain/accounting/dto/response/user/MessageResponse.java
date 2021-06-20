package com.mastersessay.blockchain.accounting.dto.response.user;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Data
@Builder
public class MessageResponse implements Serializable {
    private String message;
}
