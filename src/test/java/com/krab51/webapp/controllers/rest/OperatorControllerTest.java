package com.krab51.webapp.controllers.rest;

import com.krab51.webapp.controllers.common.ErrorController;
import com.krab51.webapp.controllers.rest.base.RestControllerTest;
import com.krab51.webapp.controllers.rest.impl.OperatorControllerImpl;
import com.krab51.webapp.domain.Operator;
import com.krab51.webapp.dto.OperatorDto;
import com.krab51.webapp.services.OperatorService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;

import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.isA;
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

@WebMvcTest(controllers = { OperatorControllerImpl.class, ErrorController.class })
class OperatorControllerTest extends RestControllerTest<OperatorControllerImpl> {
    @MockBean
    OperatorService operatorService;

    @Test
    void save_positiveTest() throws Exception {
        doNothing().when(operatorService).save(any());

        mockMvc.perform(post("/api/operator").contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new OperatorDto())))
                .andExpect(status().isCreated());

        verify(operatorService, times(1)).save(isA(OperatorDto.class));
    }

    @Test
    void save_negativeTest() throws Exception {
        doThrow(new IllegalArgumentException("msg")).when(operatorService).save(isA(OperatorDto.class));

        mockMvc.perform(post("/api/operator").contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new Operator())))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("detail", is("msg")));

        verify(operatorService, times(1)).save(isA(OperatorDto.class));
    }

    @Test
    void deleteById_positiveTest() throws Exception {
        doNothing().when(operatorService).deleteById(1L);

        mockMvc.perform(delete("/api/operator/1").contentType(APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(operatorService, times(1)).deleteById(1L);
    }

    @Test
    void deleteById_negativeTest_badRequest() throws Exception {
        mockMvc.perform(delete("/api/operator/null").contentType(APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("detail", is("Failed to convert 'id' with value: 'null'")));

        verify(operatorService, times(0)).deleteById(any());
    }

    @Test
    void deleteById_negativeTest_internalError() throws Exception {
        doThrow(new IllegalArgumentException("msg")).when(operatorService).deleteById(123L);

        mockMvc.perform(delete("/api/operator/123").contentType(APPLICATION_JSON))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("detail", is("msg")));

        verify(operatorService, times(1)).deleteById(123L);
    }

    @Test
    void findById_PositiveTest() throws Exception {
        OperatorDto operator = new OperatorDto();
        operator.id = 1L;
        operator.userName = "testOperator";

        when(operatorService.findById(1L)).thenReturn(Optional.of(operator));

        mockMvc.perform(get("/api/operator/1").contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("id", is(1)))
                .andExpect(jsonPath("userName", is("testOperator")));
    }

    @Test
    void findById_negativeTest_badRequest() throws Exception {
        mockMvc.perform(get("/api/operator/null").contentType(APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("detail", is("Failed to convert 'id' with value: 'null'")));

        verify(operatorService, times(0)).findById(any());
    }

    @Test
    void findById_negativeTest_internalError() throws Exception {
        doThrow(new RuntimeException("msg")).when(operatorService).findById(123L);

        mockMvc.perform(get("/api/operator/123").contentType(APPLICATION_JSON))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("detail", is("msg")));

        verify(operatorService, times(1)).findById(123L);
    }

    @Test
    void findAll_PositiveTest() throws Exception {
        OperatorDto testOperator1 = new OperatorDto();
        testOperator1.id = 1L;
        testOperator1.userName = "testOperator1";

        OperatorDto testOperator2 = new OperatorDto();
        testOperator2.id = 2L;
        testOperator2.userName = "testOperator2";

        when(operatorService.findAll(0, 10)).thenReturn(new PageImpl<>(List.of(testOperator1, testOperator2)));

        mockMvc.perform(get("/api/operators?page=0&size=10").contentType(APPLICATION_JSON)).andExpect(status().isOk())
                .andExpect(jsonPath("content[0].id", is(1)))
                .andExpect(jsonPath("content[0].userName", is("testOperator1")))
                .andExpect(jsonPath("content[1].id", is(2)))
                .andExpect(jsonPath("content[1].userName", is("testOperator2")))
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
        doThrow(new RuntimeException("msg")).when(operatorService).findAll(0, 10);

        mockMvc.perform(get("/api/operators?page=0&size=10").contentType(APPLICATION_JSON))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("detail", is("msg")));

        verify(operatorService, times(1)).findAll(0, 10);
    }
}