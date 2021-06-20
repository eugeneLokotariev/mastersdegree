package com.mastersessay.blockchain.accounting.service;

import com.mastersessay.blockchain.accounting.model.dictionary.Manufacturer;
import com.mastersessay.blockchain.accounting.repository.AdditionalSearchableRepository;
import com.mastersessay.blockchain.accounting.repository.UserRepository;
import com.mastersessay.blockchain.accounting.util.PageUtils;
import com.mastersessay.blockchain.accounting.util.converters.GenericConvertable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.mastersessay.blockchain.accounting.consts.BlockchainAccountingConstants.BusinessMessages.*;

@SuppressWarnings("Duplicates")
@Component
public class GenericCrudProcessor<EntityId, Entity, Request, Response> {
    private static final Logger log = LoggerFactory.getLogger(GenericCrudProcessor.class);

    private final PageUtils pageUtils;
    private final UserRepository userRepository;

    public Response getById(EntityId id,
                            AdditionalSearchableRepository<Entity, EntityId> targetRepository,
                            GenericConvertable<Entity, Request, Response> converter) {
        return targetRepository
                .getById(id)
                .map(converter::buildResponseFromEntity)
                .orElseThrow(() -> new EntityNotFoundException(OBJECT_NOT_FOUND_MESSAGE));
    }

    public List<Response> getAll(Integer start,
                                 Integer count,
                                 String sortBy,
                                 String sortType,
                                 AdditionalSearchableRepository<Entity, EntityId> targetRepository,
                                 GenericConvertable<Entity, Request, Response> converter) {
        PageRequest pageRequest = pageUtils.formPageRequest(start, count, sortBy, sortType);
        Page<Entity> pageResult = targetRepository.findAll(pageRequest);

        if (pageResult.hasContent()) {
            return pageResult
                    .getContent()
                    .stream()
                    .map(converter::buildResponseFromEntity)
                    .collect(Collectors.toList());
        } else {
            return new ArrayList<>();
        }
    }

    public Response create(Request request,
                           UserDetails user,
                           Manufacturer manufacturer,
                           AdditionalSearchableRepository<Entity, EntityId> targetRepository,
                           GenericConvertable<Entity, Request, Response> converter) {
        Entity newEntity = converter.formEntityFromRequest(
                request,
                manufacturer,
                userRepository
                        .findByUsername(user.getUsername())
                        .orElseThrow(() -> new EntityNotFoundException(OBJECT_NOT_FOUND_MESSAGE))
        );

        Entity saved = targetRepository.save(newEntity);

        log.info(SUCCESSFULLY_CREATED_MESSAGE);

        return converter.buildResponseFromEntity(saved);
    }

    public Response update(EntityId id,
                           Request request,
                           Manufacturer manufacturer,
                           UserDetails user,
                           AdditionalSearchableRepository<Entity, EntityId> targetRepository,
                           GenericConvertable<Entity, Request, Response> converter) {
        Entity foundEntityByID = targetRepository
                .getById(id)
                .orElseThrow(() -> new EntityNotFoundException(OBJECT_NOT_FOUND_MESSAGE));

        converter.updateValues(
                foundEntityByID,
                request,
                manufacturer,
                userRepository
                        .findByUsername(user.getUsername())
                        .orElseThrow(() -> new EntityNotFoundException(OBJECT_NOT_FOUND_MESSAGE)));

        Entity updated = targetRepository.save(foundEntityByID);

        log.info(SUCCESSFULLY_UPDATE_MESSAGE);

        return converter.buildResponseFromEntity(updated);
    }

    public void deleteById(EntityId id,
                           AdditionalSearchableRepository<Entity, EntityId> targetRepository) {
        boolean existsById = targetRepository.existsById(id);

        if (!existsById) {
            throw new EntityNotFoundException(OBJECT_NOT_FOUND_MESSAGE);
        }

        targetRepository.deleteById(id);

        log.info(SUCCESSFULLY_DELETED_MESSAGE);
    }

    @Autowired
    public GenericCrudProcessor(PageUtils pageUtils, UserRepository userRepository) {
        this.pageUtils = pageUtils;
        this.userRepository = userRepository;
    }
}
