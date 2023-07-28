package com.krab51.webapp.services;

import com.krab51.webapp.dto.ClientDto;
import com.krab51.webapp.mappers.ObjectMapper;
import com.krab51.webapp.repositories.ClientRepository;
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
public class ClientService {
    private final Logger logger = getLogger(ClientService.class);
    protected final ClientRepository clientRepository;
    private final ObjectMapper objectMapper;

    private final Sort SORT = Sort.by(DESC, "id");

    @Autowired
    public ClientService(ClientRepository clientRepository, ObjectMapper objectMapper) {
        this.clientRepository = clientRepository;
        this.objectMapper = objectMapper;
    }

    public void save(ClientDto clientDto) {
        logger.info("Calling save at client service");

        clientRepository.save(objectMapper.clientDtoToClient(clientDto));
    }

    public void deleteById(Long id) {
        logger.info("Calling deleteById at client service");

        clientRepository.deleteById(id);
    }

    public Optional<ClientDto> findById(Long id) {
        logger.info("Calling findById at client service");

        return clientRepository.findById(id).map(objectMapper::clientToClientDto);
    }

    public Page<ClientDto> findAll(int page, int size) {
        logger.info("Calling findAll at client service");

        return clientRepository.findAll(PageRequest.of(page, size, SORT)).map(objectMapper::clientToClientDto);
    }
}