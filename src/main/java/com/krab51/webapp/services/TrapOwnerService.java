package com.krab51.webapp.services;

import com.krab51.webapp.dto.TrapOwnerDto;
import com.krab51.webapp.mappers.ObjectMapper;
import com.krab51.webapp.repositories.TrapOwnerRepository;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static org.slf4j.LoggerFactory.getLogger;
import static org.springframework.data.domain.Sort.Direction.DESC;

@Service
@Profile("default")
public class TrapOwnerService {
    protected final TrapOwnerRepository trapOwnerRepository;
    private final Logger logger = getLogger(TrapOwnerService.class);
    private final ObjectMapper objectMapper;

    private final Sort SORT = Sort.by(DESC, "id");

    @Autowired
    public TrapOwnerService(TrapOwnerRepository trapOwnerRepository, ObjectMapper objectMapper) {
        this.trapOwnerRepository = trapOwnerRepository;
        this.objectMapper = objectMapper;
    }

    public void save(TrapOwnerDto trapOwnerDto) {
        logger.info("Calling save at trap owner service");

        trapOwnerRepository.save(objectMapper.trapOwnerDtoToTrapOwner(trapOwnerDto));
    }

    public void deleteById(Long id) {
        logger.info("Calling deleteById at trap owner service");

        trapOwnerRepository.deleteById(id);
    }

    public Optional<TrapOwnerDto> findById(Long id) {
        logger.info("Calling findById at trap owner service");

        return trapOwnerRepository.findById(id).map(objectMapper::trapOwnerToTrapOwnerDto);
    }

    public Page<TrapOwnerDto> findAll(int page, int size) {
        logger.info("Calling findAll at trap owner service");

        return trapOwnerRepository.findAll(PageRequest.of(page, size, SORT)).map(objectMapper::trapOwnerToTrapOwnerDto);
    }
}