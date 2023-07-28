package com.krab51.webapp.controllers.rest;

import com.krab51.webapp.controllers.common.ErrorController;
import com.krab51.webapp.controllers.rest.base.RestControllerTest;
import com.krab51.webapp.controllers.rest.impl.TrapOwnerControllerImpl;
import com.krab51.webapp.domain.TrapOwner;
import com.krab51.webapp.dto.OperatorDto;
import com.krab51.webapp.dto.TrapOwnerDto;
import com.krab51.webapp.services.TrapOwnerService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;

import java.util.List;
import java.util.Optional;

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

@WebMvcTest(controllers = { TrapOwnerControllerImpl.class, ErrorController.class })
class TrapOwnerControllerTest extends RestControllerTest<TrapOwnerControllerImpl> {
    @MockBean
    TrapOwnerService trapOwnerService;

    @Test
    void save_PositiveTest() throws Exception {
        doNothing().when(trapOwnerService).save(any());

        mockMvc.perform(post("/api/trapowner").contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new TrapOwner())))
                .andExpect(status().isCreated());

        verify(trapOwnerService, times(1)).save(any());
    }

    @Test
    void save_NegativeTest() throws Exception {
        doThrow(new IllegalArgumentException("msg")).when(trapOwnerService).save(any());

        mockMvc.perform(post("/api/trapowner").contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new TrapOwner())))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("detail", is("msg")));

        verify(trapOwnerService, times(1)).save(any());
    }

    @Test
    void deleteById_positiveTest() throws Exception {
        doNothing().when(trapOwnerService).deleteById(1L);

        mockMvc.perform(delete("/api/trapowner/1").contentType(APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(trapOwnerService, times(1)).deleteById(1L);
    }

    @Test
    void deleteById_negativeTest_badRequest() throws Exception {
        mockMvc.perform(delete("/api/trapowner/null").contentType(APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("detail", is("Failed to convert 'id' with value: 'null'")));

        verify(trapOwnerService, times(0)).deleteById(any());
    }

    @Test
    void deleteById_negativeTest_internalError() throws Exception {
        doThrow(new IllegalArgumentException("msg")).when(trapOwnerService).deleteById(123L);

        mockMvc.perform(delete("/api/trapowner/123").contentType(APPLICATION_JSON))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("detail", is("msg")));

        verify(trapOwnerService, times(1)).deleteById(123L);
    }

    @Test
    void findById_PositiveTest() throws Exception {
        TrapOwnerDto trapOwner = new TrapOwnerDto();
        trapOwner.id = 2L;
        trapOwner.name = "testTrapOwner";

        when(trapOwnerService.findById(2L)).thenReturn(Optional.of(trapOwner));

        mockMvc.perform(get("/api/trapowner/2").contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("id", is(2)))
                .andExpect(jsonPath("name", is("testTrapOwner")));
    }

    @Test
    void findById_negativeTest_badRequest() throws Exception {
        mockMvc.perform(get("/api/trapowner/null").contentType(APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("detail", is("Failed to convert 'id' with value: 'null'")));

        verify(trapOwnerService, times(0)).findById(any());
    }

    @Test
    void findById_negativeTest_internalError() throws Exception {
        doThrow(new RuntimeException("msg")).when(trapOwnerService).findById(123L);

        mockMvc.perform(get("/api/trapowner/123").contentType(APPLICATION_JSON))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("detail", is("msg")));

        verify(trapOwnerService, times(1)).findById(123L);
    }

    @Test
    void findAll_PositiveTest() throws Exception {
        OperatorDto operator = new OperatorDto();
        operator.id = 1L;
        operator.userName = "testOperator";

        TrapOwnerDto trapOwner1 = new TrapOwnerDto();
        trapOwner1.id = 2L;
        trapOwner1.name = "testTrapOwner1";

        TrapOwnerDto trapOwner2 = new TrapOwnerDto();
        trapOwner2.id = 3L;
        trapOwner2.name = "testTrapOwner2";

        when(trapOwnerService.findAll(0, 10)).thenReturn(new PageImpl<>(List.of(trapOwner1, trapOwner2)));

        mockMvc.perform(get("/api/trapowners?page=0&size=10").contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("content[0].id", is(2)))
                .andExpect(jsonPath("content[0].name", is("testTrapOwner1")))
                .andExpect(jsonPath("content[1].id", is(3)))
                .andExpect(jsonPath("content[1].name", is("testTrapOwner2")))
                .andExpect(jsonPath("pageable", is("INSTANCE")))
                .andExpect(jsonPath("totalPages", is(1)))
                .andExpect(jsonPath("totalElements", is(2)))
                .andExpect(jsonPath("last", is(true)))
                .andExpect(jsonPath("numberOfElements", is(2)))
                .andExpect(jsonPath("number", is(0)))
                .andExpect(jsonPath("first", is(true)))
                .andExpect(jsonPath("size", is(2)))
                .andExpect(jsonPath("sort.unsorted", is(true)))
                .andExpect(jsonPath("sort.sorted", is(false)))
                .andExpect(jsonPath("sort.empty", is(true)))
                .andExpect(jsonPath("empty", is(false)));
    }

    @Test
    void findAll_negativeTest_internalError() throws Exception {
        doThrow(new RuntimeException("msg")).when(trapOwnerService).findAll(0, 10);

        mockMvc.perform(get("/api/trapowners?page=0&size=10").contentType(APPLICATION_JSON))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("detail", is("msg")));

        verify(trapOwnerService, times(1)).findAll(0, 10);
    }
}