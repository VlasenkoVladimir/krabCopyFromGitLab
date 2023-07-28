package com.krab51.webapp.services;

import com.krab51.webapp.base.LoggerTest;
import com.krab51.webapp.domain.Operator;
import com.krab51.webapp.dto.OperatorDto;
import com.krab51.webapp.exceptions.BusinessException;
import com.krab51.webapp.mappers.ObjectMapper;
import com.krab51.webapp.mappers.ObjectMapperImpl;
import com.krab51.webapp.repositories.OperatorRepository;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = { OperatorService.class, ObjectMapperImpl.class })
@ActiveProfiles("default")
public class OperatorServiceTests extends LoggerTest<OperatorService> {
    @Autowired
    OperatorService operatorService;

    @MockBean
    OperatorRepository operatorRepository;

    @MockBean
    ObjectMapper objectMapper;
    
    @Test
    void save_PositiveTest() {
        OperatorDto operatorDto = new OperatorDto();
        operatorDto.userName = "Иван Иванов";

        Operator operator = new Operator();

        when(objectMapper.operatorDtoToOperator(operatorDto)).thenReturn(operator);

        operatorService.save(operatorDto);

        verify(operatorRepository, times(1)).save(operator);
        assertLogEquals(INFO,"Calling save at operator service");
    }

    @Test
    void save_ValidationNegativeTest() {
        OperatorDto operatorDto = new OperatorDto();
        operatorDto.userName = "Ivan Ivanov";

        Operator operator = new Operator();

        when(objectMapper.operatorDtoToOperator(operatorDto)).thenReturn(operator);

        assertThrows(BusinessException.class, () -> operatorService.save(operatorDto), "Разрешается использовать только русские буквы");
        verify(operatorRepository, times(0)).save(operator);
        assertLogEquals(INFO,"Calling save at operator service");
    }

    @Test
    void save_NegativeTest() {
        OperatorDto operatorDto = new OperatorDto();
        operatorDto.userName = "Иван Иванов";

        Operator operator = new Operator();

        when(objectMapper.operatorDtoToOperator(operatorDto)).thenReturn(operator);
        when(operatorRepository.save(operator)).thenThrow(IllegalArgumentException.class);

        assertThrows(IllegalArgumentException.class, () -> operatorService.save(operatorDto));
        verify(operatorRepository, times(1)).save(operator);
        assertLogEquals(INFO,"Calling save at operator service");
    }

    @Test
    void deleteById_PositiveTest() {
        operatorService.deleteById(1L);

        verify(operatorRepository, times(1)).deleteById(1L);
        assertLogEquals(INFO,"Calling deleteById at operator service");
    }

    @Test
    void deleteById_NegativeTest() {
        doThrow(IllegalArgumentException.class).when(operatorRepository).deleteById(1L);

        assertThrows(IllegalArgumentException.class, () -> operatorService.deleteById(1L));
        verify(operatorRepository, times(1)).deleteById(1L);
        assertLogEquals(INFO,"Calling deleteById at operator service");
    }

    @Test
    void findById_PositiveTest() {
        Operator operator = new Operator();
        OperatorDto operatorDto = new OperatorDto();

        when(operatorRepository.findById(1L)).thenReturn(Optional.of(operator));
        when(objectMapper.operatorToOperatorDto(operator)).thenReturn(operatorDto);

        Optional<OperatorDto> result = operatorService.findById(1L);

        verify(operatorRepository, times(1)).findById(1L);
        assertLogEquals(INFO,"Calling findById at operator service");
        assertEquals(result.orElse(null), operatorDto);
    }

    @Test
    void findById_NegativeTest() {
        when(operatorRepository.findById(1L)).thenThrow(IllegalArgumentException.class);

        assertThrows(IllegalArgumentException.class, () -> operatorService.findById(1L));
        verify(operatorRepository, times(1)).findById(1L);
        assertLogEquals(INFO,"Calling findById at operator service");
    }

    @Test
    void findAll_PositiveTest() {
        Operator operator1 = new Operator();
        Operator operator2 = new Operator();

        OperatorDto operatorDto1 = new OperatorDto();
        OperatorDto operatorDto2 = new OperatorDto();

        List<Operator> operators = List.of(operator1, operator2);
        Page<Operator> operatorPage = new PageImpl<>(operators);

        when(operatorRepository.findAll(any(PageRequest.class))).thenReturn(operatorPage);
        when(objectMapper.operatorToOperatorDto(operator1)).thenReturn(operatorDto1);
        when(objectMapper.operatorToOperatorDto(operator2)).thenReturn(operatorDto2);

        Page<OperatorDto> result =  operatorService.findAll(0, 10);

        verify(operatorRepository, times(1)).findAll(any(PageRequest.class));
        assertLogEquals(INFO,"Calling findAll at operator service");
        List<OperatorDto> content = result.getContent();
        assertEquals(content.get(0), operatorDto1);
        assertEquals(content.get(1), operatorDto2);
    }

    @Test
    void findAll_NegativeTest() {
        when(operatorRepository.findAll(any(PageRequest.class))).thenThrow(IllegalStateException.class);

        assertThrows(IllegalStateException.class, () -> operatorService.findAll(0 , 10));
        verify(operatorRepository, times(1)).findAll(any(PageRequest.class));
        assertLogEquals(INFO,"Calling findAll at operator service");
    }
}