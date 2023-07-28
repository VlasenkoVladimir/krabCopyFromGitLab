package com.krab51.webapp.controllers.rest;

import com.krab51.webapp.controllers.common.ErrorController;
import com.krab51.webapp.controllers.rest.base.RestControllerTest;
import com.krab51.webapp.controllers.rest.impl.TrapControllerImpl;
import com.krab51.webapp.domain.Trap;
import com.krab51.webapp.dto.TrapDto;
import com.krab51.webapp.dto.TrapOwnerDto;
import com.krab51.webapp.services.TrapService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;

import java.time.LocalDate;
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

@WebMvcTest(controllers = { TrapControllerImpl.class, ErrorController.class })
class TrapControllerTest extends RestControllerTest<TrapControllerImpl> {
    @MockBean
    TrapService trapService;

    @Test
    void save_positiveTest() throws Exception {
        doNothing().when(trapService).save(any());

        mockMvc.perform(post("/api/trap").contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new Trap())))
                .andExpect(status().isCreated());

        verify(trapService, times(1)).save(any());
    }

    @Test
    void save_negativeTest() throws Exception {
        doThrow(new IllegalArgumentException("msg")).when(trapService).save(any());

        mockMvc.perform(post("/api/trap").contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new Trap())))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("detail", is("msg")));

        verify(trapService, times(1)).save(any());
    }

    @Test
    void deleteById_positiveTest() throws Exception {
        doNothing().when(trapService).deleteById(1L);

        mockMvc.perform(delete("/api/trap/1").contentType(APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(trapService, times(1)).deleteById(1L);
    }

    @Test
    void deleteById_negativeTest_badRequest() throws Exception {
        mockMvc.perform(delete("/api/trap/null").contentType(APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("detail", is("Failed to convert 'id' with value: 'null'")));

        verify(trapService, times(0)).deleteById(any());
    }

    @Test
    void deleteById_negativeTest_internalError() throws Exception {
        doThrow(new IllegalArgumentException("msg")).when(trapService).deleteById(123L);

        mockMvc.perform(delete("/api/trap/123").contentType(APPLICATION_JSON))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("detail", is("msg")));

        verify(trapService, times(1)).deleteById(123L);
    }

    @Test
    void findById_PositiveTest() throws Exception {
        TrapOwnerDto trapOwner = new TrapOwnerDto();
        trapOwner.id = 2L;
        trapOwner.name = "testTrapOwner";

        TrapDto trap = new TrapDto();
        trap.regNumber = "testRegNumber";
        trap.regDate = LocalDate.parse("2018-12-28");
        trap.trapOwner = trapOwner;

        when(trapService.findById(3L)).thenReturn(Optional.of(trap));

        mockMvc.perform(get("/api/trap/3").contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("trapOwner.id", is(2)))
                .andExpect(jsonPath("trapOwner.name", is("testTrapOwner")))
                .andExpect(jsonPath("regNumber", is("testRegNumber")))
                .andExpect(jsonPath("regDate", is("2018-12-28")));
    }

    @Test
    void findById_negativeTest_badRequest() throws Exception {
        mockMvc.perform(get("/api/trap/null").contentType(APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("detail", is("Failed to convert 'id' with value: 'null'")));
    }

    @Test
    void findById_negativeTest_internalError() throws Exception {
        doThrow(new RuntimeException("msg")).when(trapService).findById(123L);

        mockMvc.perform(get("/api/trap/123").contentType(APPLICATION_JSON))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("detail", is("msg")));

        verify(trapService, times(1)).findById(123L);
    }

    @Test
    void findAll_positiveTest() throws Exception {
        TrapDto trap1 = new TrapDto();
        trap1.regNumber = "testRegNumber1";

        TrapDto trap2 = new TrapDto();
        trap2.regNumber = "testRegNumber2";

        when(trapService.findAll(0, 10)).thenReturn(new PageImpl<>(List.of(trap1, trap2)));

        mockMvc.perform(get("/api/traps?page=0&size=10").contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("content[0].regNumber", is("testRegNumber1")))
                .andExpect(jsonPath("content[1].regNumber", is("testRegNumber2")))
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
        doThrow(new RuntimeException("msg")).when(trapService).findAll(0, 10);

        mockMvc.perform(get("/api/traps?page=0&size=10").contentType(APPLICATION_JSON))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("detail", is("msg")));

        verify(trapService, times(1)).findAll(0, 10);
    }
}