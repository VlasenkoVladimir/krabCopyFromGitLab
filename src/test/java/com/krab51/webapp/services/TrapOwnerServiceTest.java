package com.krab51.webapp.services;

import com.krab51.webapp.base.LoggerTest;
import com.krab51.webapp.domain.TrapOwner;
import com.krab51.webapp.dto.TrapOwnerDto;
import com.krab51.webapp.mappers.ObjectMapper;
import com.krab51.webapp.mappers.ObjectMapperImpl;
import com.krab51.webapp.repositories.TrapOwnerRepository;
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

@SpringBootTest(classes = { TrapOwnerService.class, ObjectMapperImpl.class })
@ActiveProfiles("default")
class TrapOwnerServiceTest extends LoggerTest<TrapOwnerService> {
    @Autowired
    TrapOwnerService trapOwnerService;

    @MockBean
    TrapOwnerRepository trapOwnerRepository;

    @MockBean
    ObjectMapper objectMapper;
    
    @Test
    void save_PositiveTest() {
        TrapOwnerDto trapOwnerDto = new TrapOwnerDto();
        TrapOwner trapOwner = new TrapOwner();

        when(objectMapper.trapOwnerDtoToTrapOwner(trapOwnerDto)).thenReturn(trapOwner);

        trapOwnerService.save(trapOwnerDto);

        verify(trapOwnerRepository, times(1)).save(trapOwner);
        assertLogEquals(INFO,"Calling save at trap owner service");
    }

    @Test
    void save_NegativeTest() {
        TrapOwnerDto trapOwnerDto = new TrapOwnerDto();
        TrapOwner trapOwner = new TrapOwner();

        when(objectMapper.trapOwnerDtoToTrapOwner(trapOwnerDto)).thenReturn(trapOwner);
        when(trapOwnerRepository.save(trapOwner)).thenThrow(IllegalArgumentException.class);

        assertThrows(IllegalArgumentException.class, () -> trapOwnerService.save(trapOwnerDto));
        verify(trapOwnerRepository, times(1)).save(trapOwner);
        assertLogEquals(INFO,"Calling save at trap owner service");
    }

    @Test
    void deleteById_PositiveTest() {
        trapOwnerService.deleteById(1L);

        verify(trapOwnerRepository, times(1)).deleteById(1L);
        assertLogEquals(INFO,"Calling deleteById at trap owner service");
    }

    @Test
    void deleteById_NegativeTest() {
        doThrow(IllegalArgumentException.class).when(trapOwnerRepository).deleteById(1L);

        assertThrows(IllegalArgumentException.class, () -> trapOwnerService.deleteById(1L));
        verify(trapOwnerRepository, times(1)).deleteById(1L);
        assertLogEquals(INFO,"Calling deleteById at trap owner service");
    }

    @Test
    void findById_PositiveTest() {
        TrapOwner trapOwner = new TrapOwner();
        TrapOwnerDto trapOwnerDto = new TrapOwnerDto();

        when(trapOwnerRepository.findById(1L)).thenReturn(Optional.of(trapOwner));
        when(objectMapper.trapOwnerToTrapOwnerDto(trapOwner)).thenReturn(trapOwnerDto);

        Optional<TrapOwnerDto> result = trapOwnerService.findById(1L);

        verify(trapOwnerRepository, times(1)).findById(1L);
        assertLogEquals(INFO,"Calling findById at trap owner service");
        assertEquals(result.orElse(null), trapOwnerDto);
    }

    @Test
    void findById_NegativeTest() {
        when(trapOwnerRepository.findById(1L)).thenThrow(IllegalArgumentException.class);

        assertThrows(IllegalArgumentException.class, () -> trapOwnerService.findById(1L));
        verify(trapOwnerRepository, times(1)).findById(1L);
        assertLogEquals(INFO,"Calling findById at trap owner service");
    }

    @Test
    void findAll_PositiveTest() {
        TrapOwner trapOwner1 = new TrapOwner();
        TrapOwner trapOwner2 = new TrapOwner();

        TrapOwnerDto trapOwnerDto1 = new TrapOwnerDto();
        TrapOwnerDto trapOwnerDto2 = new TrapOwnerDto();

        List<TrapOwner> trapOwners = List.of(trapOwner1, trapOwner2);
        Page<TrapOwner> trapOwnerPage = new PageImpl<>(trapOwners);

        when(trapOwnerRepository.findAll(any(PageRequest.class))).thenReturn(trapOwnerPage);
        when(objectMapper.trapOwnerToTrapOwnerDto(trapOwner1)).thenReturn(trapOwnerDto1);
        when(objectMapper.trapOwnerToTrapOwnerDto(trapOwner2)).thenReturn(trapOwnerDto2);

        Page<TrapOwnerDto> result =  trapOwnerService.findAll(0, 10);

        verify(trapOwnerRepository, times(1)).findAll(any(PageRequest.class));
        assertLogEquals(INFO,"Calling findAll at trap owner service");
        List<TrapOwnerDto> content = result.getContent();
        assertEquals(content.get(0), trapOwnerDto1);
        assertEquals(content.get(1), trapOwnerDto2);
    }

    @Test
    void findAll_NegativeTest() {
        when(trapOwnerRepository.findAll(any(PageRequest.class))).thenThrow(IllegalStateException.class);

        assertThrows(IllegalStateException.class, () -> trapOwnerService.findAll(0 , 10));
        verify(trapOwnerRepository, times(1)).findAll(any(PageRequest.class));
        assertLogEquals(INFO,"Calling findAll at trap owner service");
    }
}