package com.mastersessay.blockchain.accounting.util.converters;

import com.mastersessay.blockchain.accounting.dto.request.facility.ManufacturerRequest;
import com.mastersessay.blockchain.accounting.dto.request.facility.MiningCoolingRackRequest;
import com.mastersessay.blockchain.accounting.dto.response.facility.ManufacturerResponse;
import com.mastersessay.blockchain.accounting.dto.response.facility.MiningCoolingResponse;
import com.mastersessay.blockchain.accounting.model.dictionary.Manufacturer;
import com.mastersessay.blockchain.accounting.model.dictionary.facility.MiningCoolingRack;
import com.mastersessay.blockchain.accounting.model.user.User;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
@SuppressWarnings("Duplicates")
public class ManufacturerUtils {
    public ManufacturerResponse buildResponseFromEntity(Manufacturer miningFarm) {
        return ManufacturerResponse
                .builder()
                .id(miningFarm.getId())
                .name(miningFarm.getName())
                .createdBy(miningFarm.getCreatedBy())
                .createdWhen(miningFarm.getCreatedWhen())
                .modifiedBy(miningFarm.getModifiedBy() == null ? "" : miningFarm.getModifiedBy())
                .modifiedWhen(StringUtils.isBlank(miningFarm.getModifiedWhen()) ? "" : miningFarm.getModifiedWhen())
                .build();
    }

    public Manufacturer formEntityFromRequest(ManufacturerRequest manufacturerRequest,
                                              User user) {
        return Manufacturer
                .builder()
                .name(manufacturerRequest.getName())
                .createdBy(user.getUsername())
                .createdWhen(LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy hh:mm")))
                .build();
    }

    public void updateValues(ManufacturerRequest manufacturerRequest,
                             Manufacturer manufacturer,
                             User user) {
        manufacturer.setName(manufacturerRequest.getName());
        manufacturer.setModifiedBy(user.getUsername());
        manufacturer.setModifiedWhen(LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy hh:mm")));
    }
}
