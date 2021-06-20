package com.mastersessay.blockchain.accounting.dto.response.error;

import lombok.*;

@Setter
@Getter
public class ErrorResponse extends AbstractErrorResponse {
    protected String message;

    @Builder
    public ErrorResponse(int status, String timeStamp, String path, String message) {
        super(status, timeStamp, path);
        this.message = message;
    }
}
