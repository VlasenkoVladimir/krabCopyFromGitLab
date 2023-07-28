package com.krab51.webapp.services;

import com.krab51.webapp.base.LoggerTest;
import com.krab51.webapp.domain.Trap;
import com.krab51.webapp.dto.TrapDto;
import com.krab51.webapp.mappers.ObjectMapper;
import com.krab51.webapp.mappers.ObjectMapperImpl;
import com.krab51.webapp.repositories.TrapRepository;
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

@SpringBootTest(classes = { TrapService.class, ObjectMapperImpl.class })
@ActiveProfiles("default")
class TrapServiceTest extends LoggerTest<TrapService> {
    @Autowired
    TrapService trapService;

    @MockBean
    TrapRepository trapRepository;

    @MockBean
    ObjectMapper objectMapper;

    @Test
    void save_PositiveTest() {
        TrapDto trapDto = new TrapDto();
        Trap trap = new Trap();

        when(objectMapper.trapDtoToTrap(trapDto)).thenReturn(trap);

        trapService.save(trapDto);

        verify(trapRepository, times(1)).save(trap);
        assertLogEquals(INFO,"Calling save at trap service");
    }

    @Test
    void save_NegativeTest() {
        TrapDto trapDto = new TrapDto();
        Trap trap = new Trap();

        when(objectMapper.trapDtoToTrap(trapDto)).thenReturn(trap);
        when(trapRepository.save(trap)).thenThrow(IllegalArgumentException.class);

        assertThrows(IllegalArgumentException.class, () -> trapService.save(trapDto));
        verify(trapRepository, times(1)).save(trap);
        assertLogEquals(INFO,"Calling save at trap service");
    }

    @Test
    void deleteById_PositiveTest() {
        trapService.deleteById(1L);

        verify(trapRepository, times(1)).deleteById(1L);
        assertLogEquals(INFO,"Calling deleteById at trap service");
    }

    @Test
    void deleteById_NegativeTest() {
        doThrow(IllegalArgumentException.class).when(trapRepository).deleteById(1L);

        assertThrows(IllegalArgumentException.class, () -> trapService.deleteById(1L));
        verify(trapRepository, times(1)).deleteById(1L);
        assertLogEquals(INFO,"Calling deleteById at trap service");
    }

    @Test
    void findById_PositiveTest() {
        Trap trap = new Trap();
        TrapDto trapDto = new TrapDto();

        when(trapRepository.findById(1L)).thenReturn(Optional.of(trap));
        when(objectMapper.trapToTrapDto(trap)).thenReturn(trapDto);

        Optional<TrapDto> result = trapService.findById(1L);

        verify(trapRepository, times(1)).findById(1L);
        assertLogEquals(INFO,"Calling findById at trap service");
        assertEquals(result.orElse(null), trapDto);
    }

    @Test
    void findById_NegativeTest() {
        when(trapRepository.findById(1L)).thenThrow(IllegalArgumentException.class);

        assertThrows(IllegalArgumentException.class, () -> trapService.findById(1L));
        verify(trapRepository, times(1)).findById(1L);
        assertLogEquals(INFO,"Calling findById at trap service");
    }

    @Test
    void findAll_PositiveTest() {
        Trap trap1 = new Trap();
        Trap trap2 = new Trap();

        TrapDto trapDto1 = new TrapDto();
        TrapDto trapDto2 = new TrapDto();

        List<Trap> traps = List.of(trap1, trap2);
        Page<Trap> trapPage = new PageImpl<>(traps);

        when(trapRepository.findAll(any(PageRequest.class))).thenReturn(trapPage);
        when(objectMapper.trapToTrapDto(trap1)).thenReturn(trapDto1);
        when(objectMapper.trapToTrapDto(trap2)).thenReturn(trapDto2);

        Page<TrapDto> result =  trapService.findAll(0, 10);

        verify(trapRepository, times(1)).findAll(any(PageRequest.class));
        assertLogEquals(INFO,"Calling findAll at trap service");
        List<TrapDto> content = result.getContent();
        assertEquals(content.get(0), trapDto1);
        assertEquals(content.get(1), trapDto2);
    }

    @Test
    void findAll_NegativeTest() {
        when(trapRepository.findAll(any(PageRequest.class))).thenThrow(IllegalStateException.class);

        assertThrows(IllegalStateException.class, () -> trapService.findAll(0 , 10));
        verify(trapRepository, times(1)).findAll(any(PageRequest.class));
        assertLogEquals(INFO,"Calling findAll at trap service");
    }
}