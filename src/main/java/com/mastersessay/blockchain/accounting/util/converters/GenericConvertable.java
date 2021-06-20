package com.mastersessay.blockchain.accounting.util.converters;

import com.mastersessay.blockchain.accounting.model.dictionary.Manufacturer;
import com.mastersessay.blockchain.accounting.model.user.User;

public interface GenericConvertable<Entity, Request, Response> {
    Response buildResponseFromEntity(Entity entity);

    Entity formEntityFromRequest(Request request,
                                 Manufacturer manufacturer,
                                 User userDetails);

    void updateValues(Entity entity,
                      Request miningFarmRequest,
                      Manufacturer manufacturer,
                      User user);
}
