package com.krab51.webapp.controllers.rest.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.krab51.webapp.controllers.rest.KrabOrderController;
import com.krab51.webapp.controllers.rest.base.BaseControllerImpl;
import com.krab51.webapp.dto.KrabOrderDto;
import com.krab51.webapp.services.KrabOrderService;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import static org.slf4j.LoggerFactory.getLogger;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_OCTET_STREAM;

@RestController
public class KrabOrderControllerImpl extends BaseControllerImpl implements KrabOrderController {
    private final Logger logger = getLogger(KrabOrderControllerImpl.class);
    private final KrabOrderService krabOrderService;

    @Autowired
    public KrabOrderControllerImpl(ObjectMapper objectMapper,
                                   KrabOrderService krabOrderService) {
        super(objectMapper);
        this.krabOrderService = krabOrderService;
    }

    @Override
    public ResponseEntity<HttpStatus> save(KrabOrderDto krabOrderDto) {
        logger.info("Calling save at krab order endpoint {}", toJson(krabOrderDto));

        krabOrderService.save(krabOrderDto);

        return new ResponseEntity<>(CREATED);
    }

    @Override
    public ResponseEntity<HttpStatus> deleteById(Long id) {
        logger.info("Calling deleteById at krab order endpoint with id {}", id);

        krabOrderService.deleteById(id);

        return new ResponseEntity<>(OK);
    }

    @Override
    public ResponseEntity<KrabOrderDto> findById(Long id) {
        logger.info("Calling findById at krab order endpoint with id {}", id);

        return krabOrderService.findById(id)
                .map(krabOrderDto -> new ResponseEntity<>(krabOrderDto, OK))
                .orElseGet(() -> new ResponseEntity<>(null, NO_CONTENT));
    }

    @Override
    public ResponseEntity<Page<KrabOrderDto>> findAll(int page, int size) {
        logger.info("Calling findAll at krab order endpoint with page {} and size {}", page, size);

        Page<KrabOrderDto> resultPage = krabOrderService.findAll(page, size);

        return resultPage.getContent().size() != 0
                ? new ResponseEntity<>(resultPage, OK)
                : new ResponseEntity<>(null, NO_CONTENT);
    }

    @Override
    public ResponseEntity<Resource> orderForPrint(Long id) {
        logger.info("Calling orderForPrint at order endpoint with id {}", id);

        return krabOrderService.orderForPrintById(id)
                .map(result -> generateResponse(id, result))
                .orElseGet(() -> new ResponseEntity<>(null, NO_CONTENT));
    }

    @Override
    public ResponseEntity<Resource> birkaForPrintById(Long id) {
        logger.info("Calling birkaForPrintById at order endpoint with id {}", id);

        return krabOrderService.birkaForPrintById(id)
                .map(result -> generateResponse(id, result))
                .orElseGet(() -> new ResponseEntity<>(null, NO_CONTENT));
    }

    private ResponseEntity<Resource> generateResponse(Long id, ByteArrayResource byteArrayResource) {
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + ("order-" + id + ".pdf"));
        headers.add("Cache-Control", "no-cache, no-store");
        headers.add("Expires", "0");
        return ResponseEntity.ok()
                .headers(headers)
                .contentLength(byteArrayResource.contentLength())
                .contentType(APPLICATION_OCTET_STREAM)
                .body(byteArrayResource);
    }
}