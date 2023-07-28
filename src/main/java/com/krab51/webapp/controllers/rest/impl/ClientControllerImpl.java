package com.krab51.webapp.controllers.rest.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.krab51.webapp.controllers.rest.ClientController;
import com.krab51.webapp.controllers.rest.base.BaseControllerImpl;
import com.krab51.webapp.dto.ClientDto;
import com.krab51.webapp.services.ClientService;
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
public class ClientControllerImpl extends BaseControllerImpl implements ClientController {
    private final Logger logger = getLogger(ClientControllerImpl.class);
    private final ClientService clientService;

    @Autowired
    public ClientControllerImpl(ObjectMapper objectMapper, ClientService clientService) {
        super(objectMapper);
        this.clientService = clientService;
    }

    @Override
    public ResponseEntity<HttpStatus> save(ClientDto clientDto) {
        logger.info("Calling save at client endpoint {}", toJson(clientDto));

        clientService.save(clientDto);

        return new ResponseEntity<>(CREATED);
    }

    @Override
    public ResponseEntity<HttpStatus> deleteById(Long id) {
        logger.info("Calling deleteById at client endpoint with id {}", id);

        clientService.deleteById(id);

        return new ResponseEntity<>(OK);
    }

    @Override
    public ResponseEntity<ClientDto> findById(Long id) {
        logger.info("Calling findById at client endpoint with id {}", id);

        return clientService.findById(id)
                .map(clientDto -> new ResponseEntity<>(clientDto, OK))
                .orElseGet(() -> new ResponseEntity<>(null, NO_CONTENT));
    }

    @Override
    public ResponseEntity<Page<ClientDto>> findAll(int page, int size) {
        logger.info("Calling findAll at client endpoint with page {} and size {}", page, size);

        Page<ClientDto> resultPage = clientService.findAll(page, size);

        return resultPage.getContent().size() != 0
                ? new ResponseEntity<>(resultPage, OK)
                : new ResponseEntity<>(null, NO_CONTENT);
    }
}