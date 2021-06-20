package com.mastersessay.blockchain.accounting.dto.response.error;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;

@Setter
@Getter
@AllArgsConstructor
public abstract class AbstractErrorResponse implements Serializable {
    protected int status;
    protected String timeStamp;
    protected String path;
}
