package com.krab51.webapp.controllers.rest;

import com.krab51.webapp.controllers.common.ErrorController;
import com.krab51.webapp.controllers.rest.base.RestControllerTest;
import com.krab51.webapp.controllers.rest.impl.ClientControllerImpl;
import com.krab51.webapp.domain.Client;
import com.krab51.webapp.dto.ClientDto;
import com.krab51.webapp.services.ClientService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = { ClientControllerImpl.class, ErrorController.class })
class ClientControllerTest extends RestControllerTest<ClientControllerImpl> {
    @MockBean
    ClientService clientService;

    @Test
    void save_positiveTest() throws Exception {
        doNothing().when(clientService).save(any());

        mockMvc.perform(post("/api/client").contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new Client())))
                .andExpect(status().isCreated());

        verify(clientService, times(1)).save(any());
    }

    @Test
    void save_negativeTest() throws Exception {
        doThrow(new IllegalArgumentException("msg")).when(clientService).save(any());

        mockMvc.perform(post("/api/client").contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new Client())))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("detail", is("msg")));

        verify(clientService, times(1)).save(any());
    }

    @Test
    void deleteById_positiveTest() throws Exception {
        doNothing().when(clientService).deleteById(1L);

        mockMvc.perform(delete("/api/client/1").contentType(APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(clientService, times(1)).deleteById(1L);
    }

    @Test
    void deleteById_negativeTest_badRequest() throws Exception {
        mockMvc.perform(delete("/api/client/null").contentType(APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("detail", is("Failed to convert 'id' with value: 'null'")));

        verify(clientService, times(0)).deleteById(any());
    }

    @Test
    void deleteById_negativeTest_internalError() throws Exception {
        doThrow(new IllegalArgumentException("msg")).when(clientService).deleteById(123L);

        mockMvc.perform(delete("/api/client/123").contentType(APPLICATION_JSON))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("detail", is("msg")));

        verify(clientService, times(1)).deleteById(123L);
    }

    @Test
    void findById_positiveTest() throws Exception {
        ClientDto clientDto = new ClientDto();
        clientDto.id = 1L;
        clientDto.docAuthority = "testAuthority";
        clientDto.docDate = LocalDate.parse("2018-12-27");
        clientDto.docNumber = "testNumber";
        clientDto.docType = "testType";
        clientDto.firstName = "testName";
        clientDto.middleName = "testMiddleName";
        clientDto.lastName = "testLastName";
        clientDto.phoneNumber = "testPhoneNumber";
        clientDto.registration = "testRegistration";

        when(clientService.findById(1L)).thenReturn(Optional.of(clientDto));

        mockMvc.perform(get("/api/client/1").contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("id", is(1)))
                .andExpect(jsonPath("docAuthority", is("testAuthority")))
                .andExpect(jsonPath("docDate", is("2018-12-27")))
                .andExpect(jsonPath("docNumber", is("testNumber")))
                .andExpect(jsonPath("docType", is("testType")))
                .andExpect(jsonPath("firstName", is("testName")))
                .andExpect(jsonPath("middleName", is("testMiddleName")))
                .andExpect(jsonPath("lastName", is("testLastName")))
                .andExpect(jsonPath("phoneNumber", is("testPhoneNumber")))
                .andExpect(jsonPath("registration", is("testRegistration")))
                .andExpect(jsonPath("$.*", hasSize(10)));
    }

    @Test
    void findById_negativeTest_badRequest() throws Exception {
        mockMvc.perform(get("/api/client/null").contentType(APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("detail", is("Failed to convert 'id' with value: 'null'")));

        verify(clientService, times(0)).findById(any());
    }

    @Test
    void findById_negativeTest_internalError() throws Exception {
        doThrow(new RuntimeException("msg")).when(clientService).findById(123L);

        mockMvc.perform(get("/api/client/123").contentType(APPLICATION_JSON))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("detail", is("msg")));

        verify(clientService, times(1)).findById(123L);
    }

    @Test
    void findAll_positiveTest() throws Exception {
        ClientDto clientDto1 = new ClientDto();
        clientDto1.id = 1L;
        clientDto1.firstName = "test1Name";
        clientDto1.middleName = "test1MiddleName";
        clientDto1.lastName = "test1LastName";
        clientDto1.phoneNumber = "test1PhoneNumber";

        ClientDto clientDto2 = new ClientDto();
        clientDto2.id = 2L;
        clientDto2.firstName = "test2Name";
        clientDto2.middleName = "test2MiddleName";
        clientDto2.lastName = "test2LastName";
        clientDto2.phoneNumber = "test2PhoneNumber";

        when(clientService.findAll(0, 10)).thenReturn(new PageImpl<>(List.of(clientDto1, clientDto2)));

        mockMvc.perform(get("/api/clients?page=0&size=10").contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("content[0].id", is(1)))
                .andExpect(jsonPath("content[0].firstName", is("test1Name")))
                .andExpect(jsonPath("content[0].middleName", is("test1MiddleName")))
                .andExpect(jsonPath("content[0].lastName", is("test1LastName")))
                .andExpect(jsonPath("content[0].phoneNumber", is("test1PhoneNumber")))
                .andExpect(jsonPath("content[1].id", is(2)))
                .andExpect(jsonPath("content[1].firstName", is("test2Name")))
                .andExpect(jsonPath("content[1].middleName", is("test2MiddleName")))
                .andExpect(jsonPath("content[1].lastName", is("test2LastName")))
                .andExpect(jsonPath("content[1].phoneNumber", is("test2PhoneNumber")))
                .andExpectAll(matchPage())
                .andExpect(jsonPath("$.*", hasSize(11)))
                .andExpect(jsonPath("$.content.*", hasSize(2)))
                .andExpect(jsonPath("$.content[0].*", hasSize(10)))
                .andExpect(jsonPath("$.content[1].*", hasSize(10)));
    }

    @Test
    void findAll_negativeTest_internalError() throws Exception {
        doThrow(new RuntimeException("msg")).when(clientService).findAll(0, 10);

        mockMvc.perform(get("/api/clients?page=0&size=10").contentType(APPLICATION_JSON))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("detail", is("msg")));

        verify(clientService, times(1)).findAll(0, 10);
    }
}