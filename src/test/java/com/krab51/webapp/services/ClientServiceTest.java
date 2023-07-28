package com.krab51.webapp.services;

import com.krab51.webapp.base.LoggerTest;
import com.krab51.webapp.domain.Client;
import com.krab51.webapp.dto.ClientDto;
import com.krab51.webapp.mappers.ObjectMapper;
import com.krab51.webapp.mappers.ObjectMapperImpl;
import com.krab51.webapp.repositories.ClientRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static ch.qos.logback.classic.Level.INFO;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = { ClientService.class, ObjectMapperImpl.class })
@ActiveProfiles("default")
class ClientServiceTest extends LoggerTest<ClientService> {
    @Autowired
    ClientService clientService;

    @MockBean
    ClientRepository clientRepository;

    @MockBean
    ObjectMapper objectMapper;

    @Test
    void save_PositiveTest() {
        ClientDto clientDto = new ClientDto();
        Client client = new Client();

        when(objectMapper.clientDtoToClient(clientDto)).thenReturn(client);

        clientService.save(clientDto);

        verify(clientRepository, times(1)).save(client);
        assertLogEquals(INFO,"Calling save at client service");
    }

    @Test
    void save_NegativeTest() {
        ClientDto clientDto = new ClientDto();
        Client client = new Client();

        when(objectMapper.clientDtoToClient(clientDto)).thenReturn(client);
        when(clientRepository.save(client)).thenThrow(IllegalArgumentException.class);

        assertThrows(IllegalArgumentException.class, () -> clientService.save(clientDto));
        verify(clientRepository, times(1)).save(client);
        assertLogEquals(INFO,"Calling save at client service");
    }

    @Test
    void deleteById_PositiveTest() {
        clientService.deleteById(1L);

        verify(clientRepository, times(1)).deleteById(1L);
        assertLogEquals(INFO,"Calling deleteById at client service");
    }

    @Test
    void deleteById_NegativeTest() {
        doThrow(IllegalArgumentException.class).when(clientRepository).deleteById(1L);

        assertThrows(IllegalArgumentException.class, () -> clientService.deleteById(1L));
        verify(clientRepository, times(1)).deleteById(1L);
        assertLogEquals(INFO,"Calling deleteById at client service");
    }

    @Test
    void findById_PositiveTest() {
        Client client = new Client();
        ClientDto clientDto = new ClientDto();

        when(clientRepository.findById(1L)).thenReturn(Optional.of(client));
        when(objectMapper.clientToClientDto(client)).thenReturn(clientDto);

        Optional<ClientDto> result = clientService.findById(1L);

        verify(clientRepository, times(1)).findById(1L);
        assertLogEquals(INFO,"Calling findById at client service");
        assertEquals(result.orElse(null), clientDto);
    }

    @Test
    void findById_NegativeTest() {
        when(clientRepository.findById(1L)).thenThrow(IllegalArgumentException.class);

        assertThrows(IllegalArgumentException.class, () -> clientService.findById(1L));
        verify(clientRepository, times(1)).findById(1L);
        assertLogEquals(INFO,"Calling findById at client service");
    }

    @Test
    void findAll_PositiveTest() {
        Client client1 = new Client();
        Client client2 = new Client();

        ClientDto clientDto1 = new ClientDto();
        ClientDto clientDto2 = new ClientDto();

        List<Client> clients = List.of(client1, client2);
        Page<Client> clientPage = new PageImpl<>(clients);

        when(clientRepository.findAll(any(PageRequest.class))).thenReturn(clientPage);
        when(objectMapper.clientToClientDto(client1)).thenReturn(clientDto1);
        when(objectMapper.clientToClientDto(client2)).thenReturn(clientDto2);

        Page<ClientDto> result =  clientService.findAll(0, 10);

        verify(clientRepository, times(1)).findAll(any(PageRequest.class));
        assertLogEquals(INFO,"Calling findAll at client service");
        List<ClientDto> content = result.getContent();
        assertEquals(content.get(0), clientDto1);
        assertEquals(content.get(1), clientDto2);
    }

    @Test
    void findAll_NegativeTest() {
        when(clientRepository.findAll(any(PageRequest.class))).thenThrow(IllegalStateException.class);

        assertThrows(IllegalStateException.class, () -> clientService.findAll(0 , 10));
        verify(clientRepository, times(1)).findAll(any(PageRequest.class));
        assertLogEquals(INFO,"Calling findAll at client service");
    }
}