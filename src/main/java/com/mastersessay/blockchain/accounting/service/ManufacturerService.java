package com.mastersessay.blockchain.accounting.service;

import com.mastersessay.blockchain.accounting.dto.request.facility.ManufacturerRequest;
import com.mastersessay.blockchain.accounting.dto.response.facility.ManufacturerResponse;
import com.mastersessay.blockchain.accounting.model.dictionary.Manufacturer;
import com.mastersessay.blockchain.accounting.repository.ManufacturerRepository;
import com.mastersessay.blockchain.accounting.repository.UserRepository;
import com.mastersessay.blockchain.accounting.util.PageUtils;
import com.mastersessay.blockchain.accounting.util.converters.ManufacturerUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.mastersessay.blockchain.accounting.consts.BlockchainAccountingConstants.BusinessMessages.*;

@Service
@SuppressWarnings("Duplicates")
public class ManufacturerService {
    private static final Logger log = LoggerFactory.getLogger(ManufacturerService.class);

    private final ManufacturerRepository manufacturerRepository;
    private final UserRepository userRepository;

    private final ManufacturerUtils manufacturerUtils;
    private final PageUtils pageUtils;

    @Transactional
    public ManufacturerResponse getByNameAsResponse(String manufacturerName) {
        return manufacturerRepository
                .getByName(manufacturerName)
                .map(manufacturerUtils::buildResponseFromEntity)
                .orElseThrow(() -> new EntityNotFoundException(MANUFACTURER_NOT_FOUND_MESSAGE));
    }

    @Transactional
    public List<ManufacturerResponse> getAll(Integer start,
                                             Integer count,
                                             String sortBy,
                                             String sortType) {
        PageRequest pageRequest = pageUtils.formPageRequest(start, count, sortBy, sortType);

        Page<Manufacturer> pageResult = manufacturerRepository.findAll(pageRequest);

        if (pageResult.hasContent()) {
            return pageResult
                    .getContent()
                    .stream()
                    .map(manufacturerUtils::buildResponseFromEntity)
                    .collect(Collectors.toList());
        } else {
            return new ArrayList<>();
        }
    }

    @Transactional
    public ManufacturerResponse create(ManufacturerRequest manufacturerRequest, UserDetails user) {
        Optional<Manufacturer> byName = manufacturerRepository
                .getByName(manufacturerRequest.getName());

        if (byName.isPresent()) {
            throw new IllegalArgumentException(OBJECT_EXISTS_MESSAGE);
        }

        Manufacturer newManufacturer =
                manufacturerUtils.formEntityFromRequest(manufacturerRequest,
                        userRepository
                                .findByUsername(user.getUsername())
                                .orElseThrow(() -> new EntityNotFoundException(OBJECT_NOT_FOUND_MESSAGE)));

        Manufacturer saved = manufacturerRepository.save(newManufacturer);

        log.info(SUCCESSFULLY_CREATED_MESSAGE);

        return manufacturerUtils.buildResponseFromEntity(saved);
    }

    @Transactional
    public ManufacturerResponse update(Long id, ManufacturerRequest manufacturerRequest, UserDetails user) {
        Manufacturer manufacturer = manufacturerRepository
                .getById(id)
                .orElseThrow(() -> new EntityNotFoundException(OBJECT_NOT_FOUND_MESSAGE));

        manufacturerUtils.updateValues(
                manufacturerRequest,
                manufacturer,
                userRepository
                        .findByUsername(user.getUsername())
                        .orElseThrow(() -> new EntityNotFoundException(OBJECT_NOT_FOUND_MESSAGE)));

        Manufacturer updated = manufacturerRepository.save(manufacturer);

        log.info(SUCCESSFULLY_UPDATE_MESSAGE);

        return manufacturerUtils.buildResponseFromEntity(updated);
    }

    @Transactional
    public void delete(Long id) {
        boolean existsById = manufacturerRepository.existsById(id);

        if (!existsById) {
            throw new EntityNotFoundException(OBJECT_NOT_FOUND_MESSAGE);
        }

        manufacturerRepository.deleteById(id);

        log.info(SUCCESSFULLY_DELETED_MESSAGE);
    }

    @Transactional
    public Manufacturer findOrCreateByName(ManufacturerRequest manufacturerRequest, UserDetails user) {
        return manufacturerRepository
                .getByName(manufacturerRequest.getName())
                .orElseGet(() -> manufacturerRepository.save(
                        manufacturerUtils.formEntityFromRequest(manufacturerRequest,
                                userRepository
                                        .findByUsername(user.getUsername())
                                        .orElseThrow(() -> new EntityNotFoundException(OBJECT_NOT_FOUND_MESSAGE))
                        )
                ));
    }

    @Transactional
    public Manufacturer getByName(String manufacturerName) {
        return manufacturerRepository
                .getByName(manufacturerName)
                .orElseThrow(() -> new EntityNotFoundException(MANUFACTURER_NOT_FOUND_MESSAGE));
    }

    @Autowired
    public ManufacturerService(ManufacturerRepository manufacturerRepository,
                               UserRepository userRepository,
                               ManufacturerUtils manufacturerUtils,
                               PageUtils pageUtils) {
        this.manufacturerRepository = manufacturerRepository;
        this.userRepository = userRepository;
        this.manufacturerUtils = manufacturerUtils;
        this.pageUtils = pageUtils;
    }
}
