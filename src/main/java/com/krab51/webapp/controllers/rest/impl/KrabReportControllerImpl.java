package com.krab51.webapp.controllers.rest.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.krab51.webapp.controllers.rest.KrabReportController;
import com.krab51.webapp.controllers.rest.base.BaseControllerImpl;
import com.krab51.webapp.dto.KrabReportDto;
import com.krab51.webapp.services.KrabReportService;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;

import static org.slf4j.LoggerFactory.getLogger;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.http.HttpStatus.OK;

@RestController
@Validated
public class KrabReportControllerImpl extends BaseControllerImpl implements KrabReportController {
    private final Logger logger = getLogger(KrabReportControllerImpl.class);
    private final KrabReportService krabReportService;

    @Autowired
    public KrabReportControllerImpl(ObjectMapper objectMapper, KrabReportService krabReportService) {
        super(objectMapper);
        this.krabReportService = krabReportService;
    }

    @Override
    public ResponseEntity<HttpStatus> save(KrabReportDto krabReportDto) {
        logger.info("Calling save at report endpoint {}", toJson(krabReportDto));

        krabReportService.save(krabReportDto);

        return new ResponseEntity<>(CREATED);
    }

    @Override
    public ResponseEntity<HttpStatus> deleteById(Long id) {
        logger.info("Calling deleteById at report endpoint with id {}", id);

        krabReportService.deleteById(id);

        return new ResponseEntity<>(OK);
    }

    @Override
    public ResponseEntity<KrabReportDto> findById(Long id) {
        logger.info("Calling findById at report endpoint with id {}", id);

        return krabReportService.findById(id)
                .map(krabReportDto -> new ResponseEntity<>(krabReportDto, OK))
                .orElseGet(() -> new ResponseEntity<>(null, NO_CONTENT));
    }

    @Override
    public ResponseEntity<Page<KrabReportDto>> findAll(int page, int size) {
        logger.info("Calling findAll at report endpoint with page {} and size {}", page, size);

        Page<KrabReportDto> resultPage = krabReportService.findAll(page, size);

        return resultPage.getContent().size() != 0
                ? new ResponseEntity<>(resultPage, OK)
                : new ResponseEntity<>(null, NO_CONTENT);
    }
}