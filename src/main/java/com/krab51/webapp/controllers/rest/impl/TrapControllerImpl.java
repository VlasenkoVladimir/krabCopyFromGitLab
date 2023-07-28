package com.krab51.webapp.controllers.rest.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.krab51.webapp.controllers.rest.TrapController;
import com.krab51.webapp.controllers.rest.base.BaseControllerImpl;
import com.krab51.webapp.dto.TrapDto;
import com.krab51.webapp.services.TrapService;
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
public class TrapControllerImpl extends BaseControllerImpl implements TrapController {
    private final Logger logger = getLogger(TrapControllerImpl.class);
    private final TrapService trapService;

    @Autowired
    public TrapControllerImpl(ObjectMapper objectMapper, TrapService trapService) {
        super(objectMapper);
        this.trapService = trapService;
    }

    @Override
    public ResponseEntity<HttpStatus> save(TrapDto trapDto) {
        logger.info("Calling save at trap endpoint {}", toJson(trapDto));

        trapService.save(trapDto);

        return new ResponseEntity<>(CREATED);
    }

    @Override
    public ResponseEntity<HttpStatus> deleteById(Long id) {
        logger.info("Calling deleteById at trap endpoint with id {}", id);

        trapService.deleteById(id);

        return new ResponseEntity<>(OK);
    }

    @Override
    public ResponseEntity<TrapDto> findById(Long id) {
        logger.info("Calling findById at trap endpoint with id {}", id);

        return trapService.findById(id)
                .map(trapDto -> new ResponseEntity<>(trapDto, OK))
                .orElseGet(() -> new ResponseEntity<>(null, NO_CONTENT));
    }

    @Override
    public ResponseEntity<TrapDtoPage> findAll(int page, int size) {
        logger.info("Calling findAll at trap endpoint with page {} and size {}", page, size);

        Page<TrapDto> resultPage = trapService.findAll(page, size);

        return resultPage.getContent().size() != 0
                ? new ResponseEntity<>(new TrapController.TrapDtoPage(resultPage.getContent(),
                resultPage.getPageable(), resultPage.getTotalElements()), OK)
                : new ResponseEntity<>(null, NO_CONTENT);
    }
}