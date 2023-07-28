package com.krab51.webapp.controllers.rest.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.krab51.webapp.controllers.rest.TrapOwnerController;
import com.krab51.webapp.controllers.rest.base.BaseControllerImpl;
import com.krab51.webapp.dto.TrapOwnerDto;
import com.krab51.webapp.services.TrapOwnerService;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import static org.slf4j.LoggerFactory.getLogger;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.http.HttpStatus.OK;

@RestController
public class TrapOwnerControllerImpl extends BaseControllerImpl implements TrapOwnerController {
    private final Logger logger = getLogger(TrapOwnerControllerImpl.class);
    private final TrapOwnerService trapOwnerService;

    @Autowired
    public TrapOwnerControllerImpl(ObjectMapper objectMapper, TrapOwnerService trapOwnerService) {
        super(objectMapper);
        this.trapOwnerService = trapOwnerService;
    }

    @Override
    public ResponseEntity<HttpStatus> save(TrapOwnerDto trapOwnerDto) {
        logger.info("Calling save at trap owner endpoint {}", toJson(trapOwnerDto));

        trapOwnerService.save(trapOwnerDto);

        return new ResponseEntity<>(CREATED);
    }

    @Override
    public ResponseEntity<HttpStatus> deleteById(Long id) {
        logger.info("Calling deleteById at trap owner endpoint with id {}", id);

        trapOwnerService.deleteById(id);

        return new ResponseEntity<>(OK);
    }

    @Override
    public ResponseEntity<TrapOwnerDto> findById(Long id) {
        logger.info("Calling findById at trap owner endpoint with id {}", id);

        return trapOwnerService.findById(id)
                .map(trapOwnerDto -> new ResponseEntity<>(trapOwnerDto, OK))
                .orElseGet(() -> new ResponseEntity<>(null, NO_CONTENT));
    }

    @Override
    public ResponseEntity<TrapOwnerDtoPage> findAll(int page, int size) {
        logger.info("Calling findAll at trap owner endpoint with page {} and size {}", page, size);

        Page<TrapOwnerDto> resultPage = trapOwnerService.findAll(page, size);

        return resultPage.getContent().size() != 0
                ? new ResponseEntity<>(new TrapOwnerDtoPage(resultPage.getContent(), resultPage.getPageable(),
                resultPage.getTotalElements()), OK)
                : new ResponseEntity<>(null, NO_CONTENT);
    }
}