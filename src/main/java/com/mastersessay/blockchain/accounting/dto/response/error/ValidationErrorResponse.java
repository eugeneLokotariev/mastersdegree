package com.mastersessay.blockchain.accounting.dto.response.error;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class ValidationErrorResponse extends AbstractErrorResponse {
    private List<Violation> violations;

    @Builder
    public ValidationErrorResponse(int status, String timeStamp, String path, List<Violation> violations) {
        super(status, timeStamp, path);
        this.violations = violations;
    }
}
