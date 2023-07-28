package com.krab51.webapp.services;

import com.krab51.webapp.base.LoggerTest;
import com.krab51.webapp.domain.KrabOrder;
import com.krab51.webapp.dto.KrabOrderDto;
import com.krab51.webapp.mappers.ObjectMapper;
import com.krab51.webapp.mappers.ObjectMapperImpl;
import com.krab51.webapp.repositories.KrabOrderRepository;
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

@SpringBootTest(classes = {KrabOrderService.class, ObjectMapperImpl.class})
@ActiveProfiles("default")
class KrabOrderServiceTest extends LoggerTest<KrabOrderService> {
    @Autowired
    KrabOrderService krabOrderService;

    @MockBean
    KrabOrderRepository krabOrderRepository;

    @MockBean
    ObjectMapper objectMapper;

    @Test
    void save_PositiveTest() {
        KrabOrderDto krabOrderDto = new KrabOrderDto();
        KrabOrder krabOrder = new KrabOrder();

        when(objectMapper.krabOrderDtoToKrabOrder(krabOrderDto)).thenReturn(krabOrder);

        krabOrderService.save(krabOrderDto);

        verify(krabOrderRepository, times(1)).save(krabOrder);
        assertLogEquals(INFO,"Calling save at krab order service");
    }

    @Test
    void save_NegativeTest() {
        KrabOrderDto krabOrderDto = new KrabOrderDto();
        KrabOrder krabOrder = new KrabOrder();

        when(objectMapper.krabOrderDtoToKrabOrder(krabOrderDto)).thenReturn(krabOrder);
        when(krabOrderRepository.save(krabOrder)).thenThrow(IllegalArgumentException.class);

        assertThrows(IllegalArgumentException.class, () -> krabOrderService.save(krabOrderDto));
        verify(krabOrderRepository, times(1)).save(krabOrder);
        assertLogEquals(INFO,"Calling save at krab order service");
    }

    @Test
    void deleteById_PositiveTest() {
        krabOrderService.deleteById(1L);

        verify(krabOrderRepository, times(1)).deleteById(1L);
        assertLogEquals(INFO,"Calling deleteById at krab order service");
    }

    @Test
    void deleteById_NegativeTest() {
        doThrow(IllegalArgumentException.class).when(krabOrderRepository).deleteById(1L);

        assertThrows(IllegalArgumentException.class, () -> krabOrderService.deleteById(1L));
        verify(krabOrderRepository, times(1)).deleteById(1L);
        assertLogEquals(INFO,"Calling deleteById at krab order service");
    }

    @Test
    void findById_PositiveTest() {
        KrabOrder krabOrder = new KrabOrder();
        KrabOrderDto krabOrderDto = new KrabOrderDto();

        when(krabOrderRepository.findById(1L)).thenReturn(Optional.of(krabOrder));
        when(objectMapper.krabOrderToKrabOrderDto(krabOrder)).thenReturn(krabOrderDto);

        Optional<KrabOrderDto> result = krabOrderService.findById(1L);

        verify(krabOrderRepository, times(1)).findById(1L);
        assertLogEquals(INFO,"Calling findById at krab order service");
        assertEquals(result.orElse(null), krabOrderDto);
    }

    @Test
    void findById_NegativeTest() {
        when(krabOrderRepository.findById(1L)).thenThrow(IllegalArgumentException.class);

        assertThrows(IllegalArgumentException.class, () -> krabOrderService.findById(1L));
        verify(krabOrderRepository, times(1)).findById(1L);
        assertLogEquals(INFO,"Calling findById at krab order service");
    }

    @Test
    void findAll_PositiveTest() {
        KrabOrder krabOrder1 = new KrabOrder();
        KrabOrder krabOrder2 = new KrabOrder();

        KrabOrderDto krabOrderDto1 = new KrabOrderDto();
        KrabOrderDto krabOrderDto2 = new KrabOrderDto();

        List<KrabOrder> krabOrders = List.of(krabOrder1, krabOrder2);
        Page<KrabOrder> krabOrderPage = new PageImpl<>(krabOrders);

        when(krabOrderRepository.findAll(any(PageRequest.class))).thenReturn(krabOrderPage);
        when(objectMapper.krabOrderToKrabOrderDto(krabOrder1)).thenReturn(krabOrderDto1);
        when(objectMapper.krabOrderToKrabOrderDto(krabOrder2)).thenReturn(krabOrderDto2);

        Page<KrabOrderDto> result = krabOrderService.findAll(0, 10);

        verify(krabOrderRepository, times(1)).findAll(any(PageRequest.class));
        assertLogEquals(INFO,"Calling findAll at krab order service");
        List<KrabOrderDto> content = result.getContent();
        assertEquals(content.get(0), krabOrderDto1);
        assertEquals(content.get(1), krabOrderDto2);
    }

    @Test
    void findAll_NegativeTest() {
        when(krabOrderRepository.findAll(any(PageRequest.class))).thenThrow(IllegalStateException.class);

        assertThrows(IllegalStateException.class, () -> krabOrderService.findAll(0, 10));
        verify(krabOrderRepository, times(1)).findAll(any(PageRequest.class));
        assertLogEquals(INFO,"Calling findAll at krab order service");
    }
}