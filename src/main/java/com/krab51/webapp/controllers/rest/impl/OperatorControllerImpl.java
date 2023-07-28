package com.krab51.webapp.controllers.rest.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.krab51.webapp.controllers.rest.OperatorController;
import com.krab51.webapp.controllers.rest.base.BaseControllerImpl;
import com.krab51.webapp.dto.OperatorDto;
import com.krab51.webapp.services.OperatorService;
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
public class OperatorControllerImpl extends BaseControllerImpl implements OperatorController {
    private final Logger logger = getLogger(OperatorControllerImpl.class);
    private final OperatorService operatorService;

    @Autowired
    public OperatorControllerImpl(ObjectMapper objectMapper, OperatorService operatorService) {
        super(objectMapper);
        this.operatorService = operatorService;
    }

    @Override
    public ResponseEntity<HttpStatus> save(OperatorDto operatorDto) {
        logger.info("Calling save at operator endpoint {}", toJson(operatorDto));

        operatorService.save(operatorDto);

        return new ResponseEntity<>(CREATED);
    }

    @Override
    public ResponseEntity<HttpStatus> deleteById(Long id) {
        logger.info("Calling deleteById at operator endpoint with id {}", id);

        operatorService.deleteById(id);

        return new ResponseEntity<>(OK);
    }

    @Override
    public ResponseEntity<OperatorDto> findById(Long id) {
        logger.info("Calling findById at operator endpoint with id {}", id);

        return operatorService.findById(id)
                .map(operatorDto -> new ResponseEntity<>(operatorDto, OK))
                .orElseGet(() -> new ResponseEntity<>(null, NO_CONTENT));
    }

    @Override
    public ResponseEntity<OperatorDtoPage> findAll(int page, int size) {
        logger.info("Calling findAll at operator endpoint with page {} and size {}", page, size);

        Page<OperatorDto> resultPage = operatorService.findAll(page, size);

        return resultPage.getContent().size() != 0
                ? new ResponseEntity<>(new OperatorController.OperatorDtoPage(resultPage.getContent(),
                resultPage.getPageable(), resultPage.getTotalElements()), OK)
                : new ResponseEntity<>(null, NO_CONTENT);
    }
}