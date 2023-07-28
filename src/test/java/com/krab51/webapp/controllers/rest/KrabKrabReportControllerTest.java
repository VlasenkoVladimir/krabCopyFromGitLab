package com.krab51.webapp.controllers.rest;

import com.krab51.webapp.controllers.common.ErrorController;
import com.krab51.webapp.controllers.rest.base.RestControllerTest;
import com.krab51.webapp.controllers.rest.impl.KrabReportControllerImpl;
import com.krab51.webapp.domain.KrabReport;
import com.krab51.webapp.dto.KrabOrderDto;
import com.krab51.webapp.dto.KrabReportDto;
import com.krab51.webapp.services.KrabReportService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
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

@WebMvcTest(controllers = { KrabReportControllerImpl.class, ErrorController.class })
class KrabKrabReportControllerTest extends RestControllerTest<KrabReportControllerImpl> {
    @MockBean
    KrabReportService krabReportService;

    @Test
    void save_positiveTest() throws Exception {
        doNothing().when(krabReportService).save(any());

        mockMvc.perform(post("/api/report").contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new KrabReport())))
                .andExpect(status().isCreated());

        verify(krabReportService, times(1)).save(any());
    }

    @Test
    void save_negativeTest() throws Exception {
        doThrow(new IllegalArgumentException("msg")).when(krabReportService).save(any());

        mockMvc.perform(post("/api/report").contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new KrabReport())))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("detail", is("msg")));

        verify(krabReportService, times(1)).save(any());
    }

    @Test
    void deleteById_positiveTest() throws Exception {
        doNothing().when(krabReportService).deleteById(1L);

        mockMvc.perform(delete("/api/report/1").contentType(APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(krabReportService, times(1)).deleteById(1L);
    }

    @Test
    void deleteById_negativeTest_badRequest() throws Exception {
        mockMvc.perform(delete("/api/report/null").contentType(APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("detail", is("Failed to convert 'id' with value: 'null'")));

        verify(krabReportService, times(0)).deleteById(any());
    }

    @Test
    void deleteById_negativeTest_internalError() throws Exception {
        doThrow(new IllegalArgumentException("msg")).when(krabReportService).deleteById(123L);

        mockMvc.perform(delete("/api/report/123").contentType(APPLICATION_JSON))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("detail", is("msg")));

        verify(krabReportService, times(1)).deleteById(123L);
    }

    @Test
    void findById_PositiveTest() throws Exception {
        KrabOrderDto krabOrder = new KrabOrderDto();
        krabOrder.id = 2L;
        krabOrder.regDate = LocalDate.parse("2022-11-10");
        krabOrder.beginDate = LocalDateTime.parse("2022-11-11T10:11:30");

        KrabReportDto report = new KrabReportDto();
        report.id = 3L;
        report.startDate = LocalDateTime.parse("2018-12-30T15:30:00");
        report.endDate = LocalDateTime.parse("2018-12-31T15:30:00");
        report.actuallyCnt = 2;
        report.actuallyKgs = BigDecimal.valueOf(11);
        report.releasedCnt = 18;

        when(krabReportService.findById(3L)).thenReturn(Optional.of(report));

        mockMvc.perform(get("/api/report/3").contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("id", is(3)))
                .andExpect(jsonPath("startDate", is("2018-12-30T15:30:00")))
                .andExpect(jsonPath("endDate", is("2018-12-31T15:30:00")))
                .andExpect(jsonPath("actuallyCnt", is(2)))
                .andExpect(jsonPath("actuallyKgs", is(11)))
                .andExpect(jsonPath("releasedCnt", is(18)));
    }

    @Test
    void findById_negativeTest_badRequest() throws Exception {
        mockMvc.perform(get("/api/report/null").contentType(APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("detail", is("Failed to convert 'id' with value: 'null'")));

        verify(krabReportService, times(0)).findById(any());
    }

    @Test
    void findById_negativeTest_internalError() throws Exception {
        doThrow(new RuntimeException("msg")).when(krabReportService).findById(123L);

        mockMvc.perform(get("/api/report/123").contentType(APPLICATION_JSON))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("detail", is("msg")));

        verify(krabReportService, times(1)).findById(123L);
    }

    @Test
    void findAll_positiveTest() throws Exception {
        KrabReportDto report1 = new KrabReportDto();
        report1.id = 3L;
        report1.startDate = LocalDateTime.parse("2018-12-26T15:30:00");
        report1.endDate = LocalDateTime.parse("2018-12-27T15:30:00");
        report1.actuallyCnt = 2;
        report1.actuallyKgs = BigDecimal.valueOf(11);
        report1.releasedCnt = 18;

        KrabReportDto report2 = new KrabReportDto();
        report2.id = 5L;
        report2.startDate = LocalDateTime.parse("2018-12-30T15:30:00");
        report2.endDate = LocalDateTime.parse("2018-12-31T15:30:00");
        report2.actuallyCnt = 3;
        report2.actuallyKgs = BigDecimal.valueOf(12);
        report2.releasedCnt = 15;

        when(krabReportService.findAll(0, 10)).thenReturn(new PageImpl<>(List.of(report1, report2)));

        mockMvc.perform(get("/api/reports?page=0&size=10").contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("content[0].id", is(3)))
                .andExpect(jsonPath("content[0].startDate", is("2018-12-26T15:30:00")))
                .andExpect(jsonPath("content[0].endDate", is("2018-12-27T15:30:00")))
                .andExpect(jsonPath("content[0].actuallyCnt", is(2)))
                .andExpect(jsonPath("content[0].actuallyKgs", is(11)))
                .andExpect(jsonPath("content[0].releasedCnt", is(18)))
                .andExpect(jsonPath("content[1].id", is(5)))
                .andExpect(jsonPath("content[1].startDate", is("2018-12-30T15:30:00")))
                .andExpect(jsonPath("content[1].endDate", is("2018-12-31T15:30:00")))
                .andExpect(jsonPath("content[1].actuallyCnt", is(3)))
                .andExpect(jsonPath("content[1].actuallyKgs", is(12)))
                .andExpect(jsonPath("content[1].releasedCnt", is(15)))
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
        doThrow(new RuntimeException("msg")).when(krabReportService).findAll(0, 10);

        mockMvc.perform(get("/api/reports?page=0&size=10").contentType(APPLICATION_JSON))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("detail", is("msg")));

        verify(krabReportService, times(1)).findAll(0, 10);
    }
}