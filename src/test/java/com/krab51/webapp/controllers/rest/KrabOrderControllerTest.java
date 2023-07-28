package com.krab51.webapp.controllers.rest;

import com.krab51.webapp.controllers.common.ErrorController;
import com.krab51.webapp.controllers.rest.base.RestControllerTest;
import com.krab51.webapp.controllers.rest.impl.KrabOrderControllerImpl;
import com.krab51.webapp.domain.KrabOrder;
import com.krab51.webapp.dto.ClientDto;
import com.krab51.webapp.dto.KrabOrderDto;
import com.krab51.webapp.services.KrabOrderService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static com.krab51.webapp.domain.enums.KrabOrderStatus.EXPIRED;
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

@WebMvcTest(controllers = { KrabOrderControllerImpl.class, ErrorController.class })
class KrabOrderControllerTest extends RestControllerTest<KrabOrderControllerImpl> {
    @MockBean
    KrabOrderService krabOrderService;

    @Test
    void save_positiveTest() throws Exception {
        doNothing().when(krabOrderService).save(any());

        KrabOrder krabOrder = new KrabOrder();
        krabOrder.beginDate = LocalDateTime.now();

        mockMvc.perform(post("/api/kraborder").contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(krabOrder)))
                .andExpect(status().isCreated());

        verify(krabOrderService, times(1)).save(any());
    }

    @Test
    void save_negativeTest() throws Exception {
        doThrow(new IllegalArgumentException("msg")).when(krabOrderService).save(any());

        KrabOrder krabOrder = new KrabOrder();
        krabOrder.beginDate = LocalDateTime.now();

        mockMvc.perform(post("/api/kraborder").contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(krabOrder)))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("detail", is("msg")));

        verify(krabOrderService, times(1)).save(any());
    }

    @Test
    void deleteById_positiveTest() throws Exception {
        doNothing().when(krabOrderService).deleteById(1L);

        mockMvc.perform(delete("/api/kraborder/1").contentType(APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(krabOrderService, times(1)).deleteById(1L);
    }

    @Test
    void deleteById_negativeTest_badRequest() throws Exception {
        mockMvc.perform(delete("/api/kraborder/null").contentType(APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("detail", is("Failed to convert 'id' with value: 'null'")));

        verify(krabOrderService, times(0)).deleteById(any());
    }

    @Test
    void deleteById_negativeTest_internalError() throws Exception {
        doThrow(new IllegalArgumentException("msg")).when(krabOrderService).deleteById(123L);

        mockMvc.perform(delete("/api/kraborder/123").contentType(APPLICATION_JSON))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("detail", is("msg")));

        verify(krabOrderService, times(1)).deleteById(123L);
    }

    @Test
    void findById_positiveTest() throws Exception {
        ClientDto client = new ClientDto();
        client.firstName = "Иван";
        client.middleName = "Иванович";
        client.lastName = "Иванов";

        KrabOrderDto krabOrder = new KrabOrderDto();
        krabOrder.id = 1L;
        krabOrder.regDate = LocalDate.parse("2022-11-09");
        krabOrder.client = client;
        krabOrder.beginDate = LocalDateTime.parse("2022-11-10T10:11:30");
        krabOrder.status = EXPIRED;

        when(krabOrderService.findById(1L)).thenReturn(Optional.of(krabOrder));

        mockMvc.perform(get("/api/kraborder/1").contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("id", is(1)))
                .andExpect(jsonPath("regDate", is("2022-11-09")))
                .andExpect(jsonPath("client.firstName", is("Иван")))
                .andExpect(jsonPath("client.middleName", is("Иванович")))
                .andExpect(jsonPath("client.lastName", is("Иванов")))
                .andExpect(jsonPath("beginDate", is("2022-11-10T10:11:30")))
                .andExpect(jsonPath("endDate").doesNotExist())
                .andExpect(jsonPath("status", is("EXPIRED")));
    }

    @Test
    void findById_negativeTest_badRequest() throws Exception {
        mockMvc.perform(get("/api/kraborder/null").contentType(APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("detail", is("Failed to convert 'id' with value: 'null'")));

        verify(krabOrderService, times(0)).findById(any());
    }

    @Test
    void findById_negativeTest_internalError() throws Exception {
        doThrow(new RuntimeException("msg")).when(krabOrderService).findById(123L);

        mockMvc.perform(get("/api/kraborder/123").contentType(APPLICATION_JSON))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("detail", is("msg")));

        verify(krabOrderService, times(1)).findById(123L);
    }

    @Test
    void findAll_positiveTest() throws Exception {
        ClientDto client = new ClientDto();
        client.firstName = "Иван";
        client.middleName = "Иванович";
        client.lastName = "Иванов";

        KrabOrderDto krabOrder1 = new KrabOrderDto();
        krabOrder1.id = 1L;
        krabOrder1.regDate = LocalDate.parse("2022-11-09");
        krabOrder1.client = client;
        krabOrder1.beginDate = LocalDateTime.parse("2022-11-10T10:11:30");
        krabOrder1.status = EXPIRED;

        KrabOrderDto krabOrder2 = new KrabOrderDto();
        krabOrder2.id = 2L;
        krabOrder2.regDate = LocalDate.parse("2022-11-10");
        krabOrder2.client = client;
        krabOrder2.beginDate = LocalDateTime.parse("2022-11-11T10:11:30");
        krabOrder2.status = EXPIRED;

        when(krabOrderService.findAll(0, 10)).thenReturn(new PageImpl<>(List.of(krabOrder1, krabOrder2)));

        mockMvc.perform(get("/api/kraborders?page=0&size=10").contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("content[0].id", is(1)))
                .andExpect(jsonPath("content[0].regDate", is("2022-11-09")))
                .andExpect(jsonPath("content[0].client.firstName", is("Иван")))
                .andExpect(jsonPath("content[0].client.middleName", is("Иванович")))
                .andExpect(jsonPath("content[0].client.lastName", is("Иванов")))
                .andExpect(jsonPath("content[0].beginDate", is("2022-11-10T10:11:30")))
                .andExpect(jsonPath("content[0].endDate").doesNotExist())
                .andExpect(jsonPath("content[0].status", is("EXPIRED")))
                .andExpect(jsonPath("content[1].id", is(2)))
                .andExpect(jsonPath("content[1].regDate", is("2022-11-10")))
                .andExpect(jsonPath("content[1].client.firstName", is("Иван")))
                .andExpect(jsonPath("content[1].client.middleName", is("Иванович")))
                .andExpect(jsonPath("content[1].client.lastName", is("Иванов")))
                .andExpect(jsonPath("content[1].beginDate", is("2022-11-11T10:11:30")))
                .andExpect(jsonPath("content[1].endDate").doesNotExist())
                .andExpect(jsonPath("content[1].status", is("EXPIRED")))
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
        doThrow(new RuntimeException("msg")).when(krabOrderService).findAll(0, 10);

        mockMvc.perform(get("/api/kraborders?page=0&size=10").contentType(APPLICATION_JSON))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("detail", is("msg")));

        verify(krabOrderService, times(1)).findAll(0, 10);
    }
}