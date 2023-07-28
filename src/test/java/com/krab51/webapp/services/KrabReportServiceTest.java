package com.krab51.webapp.services;

import com.krab51.webapp.base.LoggerTest;
import com.krab51.webapp.domain.KrabReport;
import com.krab51.webapp.dto.KrabOrderDto;
import com.krab51.webapp.dto.KrabReportDto;
import com.krab51.webapp.exceptions.BusinessException;
import com.krab51.webapp.mappers.ObjectMapper;
import com.krab51.webapp.mappers.ObjectMapperImpl;
import com.krab51.webapp.repositories.KrabReportRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static ch.qos.logback.classic.Level.INFO;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = { KrabReportService.class, ObjectMapperImpl.class, KrabOrderService.class })
@ActiveProfiles("default")
class KrabReportServiceTest extends LoggerTest<KrabReportService> {
    @Autowired
    KrabReportService krabReportService;

    @MockBean
    KrabReportRepository krabReportRepository;

    @MockBean
    KrabOrderService krabOrderService;

    @MockBean
    ObjectMapper objectMapper;

    @Test
    void save_PositiveTest() {
        KrabReportDto krabReportDto = new KrabReportDto();

        krabReportDto.krabOrder = new KrabOrderDto();
        krabReportDto.krabOrder.id = 1L;
        krabReportDto.startDate = LocalDateTime.now().minusDays(1);
        krabReportDto.krabOrder.beginDate  = LocalDateTime.now().minusDays(2);
        krabReportDto.krabOrder.enlistedCnt = 2;
        krabReportDto.actuallyCnt = 2;

        when(krabOrderService.findById(1L)).thenReturn(Optional.of(krabReportDto.krabOrder));

        krabReportService.save(krabReportDto);

        verify(krabReportRepository, times(1)).save(any());
        assertLogEquals(INFO,"Calling save at report service");
    }

    @Test
    void save_NegativeTest_UnknownId() {
        KrabReportDto krabReportDto = new KrabReportDto();

        krabReportDto.krabOrder = new KrabOrderDto();

        BusinessException ex = assertThrows(BusinessException.class, () -> krabReportService.save(krabReportDto));

        assertEquals("Order with this id not found", ex.getMessage());
        assertLogEquals(INFO,"Calling save at report service");
    }

    @Test
    void save_NegativeTest_BadDate() {
        KrabReportDto krabReportDto = new KrabReportDto();

        krabReportDto.krabOrder = new KrabOrderDto();
        krabReportDto.krabOrder.id = 1L;
        krabReportDto.startDate = LocalDateTime.now().minusDays(4);
        krabReportDto.krabOrder.beginDate  = LocalDateTime.now().minusDays(3);
        krabReportDto.krabOrder.enlistedCnt = 2;
        krabReportDto.actuallyCnt = 2;

        when(krabOrderService.findById(1L)).thenReturn(Optional.of(krabReportDto.krabOrder));

        BusinessException ex = assertThrows(BusinessException.class, () -> krabReportService.save(krabReportDto));

        assertEquals("Not valid start catching date", ex.getMessage());
        assertLogEquals(INFO,"Calling save at report service");
    }

    @Test
    void save_NegativeTest_TooManyKrabs() {
        KrabOrderDto krabOrderDto = new KrabOrderDto();
        krabOrderDto.id = 1L;
        krabOrderDto.beginDate  = LocalDateTime.now().minusDays(6);
        krabOrderDto.enlistedCnt = 2; // сколько можно выловить

        KrabReport krabReport = new KrabReport();
        krabReport.id = 3L;
        krabReport.startDate = LocalDateTime.now().minusDays(4);
        krabReport.actuallyCnt = 1; // сколько выловлено по факту

        KrabReportDto krabReportDto = new KrabReportDto();
        krabReportDto.id = 4L;
        krabReportDto.krabOrder = krabOrderDto;
        krabReportDto.startDate = LocalDateTime.now().minusDays(5);
        krabReportDto.actuallyCnt = 2; // сколько выловлено по факту

        when(krabOrderService.findById(1L)).thenReturn(Optional.of(krabOrderDto));
        when(krabReportRepository.findByKrabOrderId(1L)).thenReturn(List.of(krabReport));

        BusinessException ex = assertThrows(BusinessException.class, () -> krabReportService.save(krabReportDto));

        assertEquals("Too many krabs", ex.getMessage());
        assertLogEquals(INFO,"Calling save at report service");
    }

    @Test
    void deleteById_PositiveTest() {
        krabReportService.deleteById(1L);

        verify(krabReportRepository, times(1)).deleteById(1L);
        assertLogEquals(INFO,"Calling deleteById at report service");
    }

    @Test
    void deleteById_NegativeTest() {
        doThrow(IllegalArgumentException.class).when(krabReportRepository).deleteById(1L);

        assertThrows(IllegalArgumentException.class, () -> krabReportService.deleteById(1L));
        verify(krabReportRepository, times(1)).deleteById(1L);
        assertLogEquals(INFO,"Calling deleteById at report service");
    }

    @Test
    void findById_PositiveTest() {
        KrabReport krabReport = new KrabReport();
        KrabReportDto krabReportDto = new KrabReportDto();

        when(krabReportRepository.findById(1L)).thenReturn(Optional.of(krabReport));
        when(objectMapper.krabReportToKrabReportDto(krabReport)).thenReturn(krabReportDto);

        Optional<KrabReportDto> result = krabReportService.findById(1L);

        verify(krabReportRepository, times(1)).findById(1L);
        assertLogEquals(INFO,"Calling findById at report service");
        assertEquals(result.orElse(null), krabReportDto);
    }

    @Test
    void findById_NegativeTest() {
        when(krabReportRepository.findById(1L)).thenThrow(IllegalArgumentException.class);

        assertThrows(IllegalArgumentException.class, () -> krabReportService.findById(1L));
        verify(krabReportRepository, times(1)).findById(1L);
        assertLogEquals(INFO,"Calling findById at report service");
    }

    @Test
    void findAll_PositiveTest() {
        KrabReport krabReport1 = new KrabReport();
        KrabReport krabReport2 = new KrabReport();

        KrabReportDto krabReportDto1 = new KrabReportDto();
        KrabReportDto krabReportDto2 = new KrabReportDto();

        List<KrabReport> krabReports = List.of(krabReport1, krabReport2);
        Page<KrabReport> reportPage = new PageImpl<>(krabReports);

        when(krabReportRepository.findAll(any(PageRequest.class))).thenReturn(reportPage);
        when(objectMapper.krabReportToKrabReportDto(krabReport1)).thenReturn(krabReportDto1);
        when(objectMapper.krabReportToKrabReportDto(krabReport2)).thenReturn(krabReportDto2);

        Page<KrabReportDto> result =  krabReportService.findAll(0, 10);

        verify(krabReportRepository, times(1)).findAll(any(PageRequest.class));
        assertLogEquals(INFO,"Calling findAll at report service");
        List<KrabReportDto> content = result.getContent();
        assertEquals(content.get(0), krabReportDto1);
        assertEquals(content.get(1), krabReportDto2);
    }

    @Test
    void findAll_NegativeTest() {
        when(krabReportRepository.findAll(any(PageRequest.class))).thenThrow(IllegalStateException.class);

        assertThrows(IllegalStateException.class, () -> krabReportService.findAll(0 , 10));
        verify(krabReportRepository, times(1)).findAll(any(PageRequest.class));
        assertLogEquals(INFO,"Calling findAll at report service");
    }
}