package com.krab51.webapp.services;

import com.krab51.webapp.dto.TrapDto;
import com.krab51.webapp.mappers.ObjectMapper;
import com.krab51.webapp.repositories.TrapRepository;
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
public class TrapService {
    private final Logger logger = getLogger(TrapService.class);
    protected final TrapRepository trapRepository;
    private final ObjectMapper objectMapper;

    private final Sort SORT = Sort.by(DESC, "regNumber");

    @Autowired
    public TrapService(TrapRepository trapRepository, ObjectMapper objectMapper) {
        this.trapRepository = trapRepository;
        this.objectMapper = objectMapper;
    }

    public void save(TrapDto trapDto) {
        logger.info("Calling save at trap service");

        trapRepository.save(objectMapper.trapDtoToTrap(trapDto));
    }

    public void deleteById(Long id) {
        logger.info("Calling deleteById at trap service");

        trapRepository.deleteById(id);
    }

    public Optional<TrapDto> findById(Long id) {
        logger.info("Calling findById at trap service");

        return trapRepository.findById(id).map(objectMapper::trapToTrapDto);
    }

    public Page<TrapDto> findAll(int page, int size) {
        logger.info("Calling findAll at trap service");

        return trapRepository.findAll(PageRequest.of(page, size, SORT)).map(objectMapper::trapToTrapDto);
    }
}