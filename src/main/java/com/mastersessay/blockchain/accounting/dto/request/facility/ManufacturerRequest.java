package com.mastersessay.blockchain.accounting.dto.request.facility;

import lombok.*;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

@Data
@Builder
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@NoArgsConstructor(access = AccessLevel.PACKAGE)
public class ManufacturerRequest implements Serializable {
    @NotBlank
    public String name;
}
