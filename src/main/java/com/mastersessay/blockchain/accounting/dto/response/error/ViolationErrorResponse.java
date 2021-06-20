package com.mastersessay.blockchain.accounting.dto.response.error;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Setter
@Getter
public class ViolationErrorResponse extends AbstractErrorResponse {
    private Map<String, String> errors;

    @Builder
    public ViolationErrorResponse(int status, String timeStamp, String path, Map<String, String> errors) {
        super(status, timeStamp, path);
        this.errors = errors;
    }
}
