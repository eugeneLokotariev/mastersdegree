package com.mastersessay.blockchain.accounting.dto.response.facility;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Data
@Builder
public class ManufacturerResponse implements Serializable {
    private Long id;
    private String name;
    private String createdBy;
    private String createdWhen;
    private String modifiedBy;
    private String modifiedWhen;
}
