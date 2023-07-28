package com.krab51.webapp.services.demo;

import com.krab51.webapp.dto.TrapDto;
import com.krab51.webapp.mappers.ObjectMapper;
import com.krab51.webapp.repositories.TrapRepository;
import com.krab51.webapp.services.TrapService;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

import static com.krab51.webapp.domain.enums.TrapStatus.UNKNOWN;
import static com.krab51.webapp.utilities.demo.RandomUtilites.getRandomNumber;
import static java.lang.String.format;

@Service
@Profile("demo")
public class TrapServiceDemo extends TrapService {
    private final TrapOwnerServiceDemo trapOwnerServiceDemo;

    @Autowired
    public TrapServiceDemo(TrapRepository trapRepository, ObjectMapper objectMapper,
                           TrapOwnerServiceDemo trapOwnerServiceDemo) {
        super(trapRepository, objectMapper);
        this.trapOwnerServiceDemo = trapOwnerServiceDemo;
    }

    @PostConstruct
    public void setUp() {
        LocalDate now = LocalDate.now();

        for (int i = 1; i <= 20; i++) saveTrap(genRegNumber(i), now.minusMonths(getRandomNumber(1, 12)));
    }

    void saveTrap(String regNumber, LocalDate regDate) {
        TrapDto trapDto = new TrapDto();

        trapDto.status = UNKNOWN;
        trapDto.regNumber = regNumber;
        trapDto.regDate = regDate;
        trapDto.trapOwner = trapOwnerServiceDemo.getRandomTrapOwner();

        save(trapDto);
    }

    String genRegNumber(int id) {
        return format("%03d", id)
                + "-"
                + format("%02d", getRandomNumber(1, 99))
                + "-"
                + format("%04d", getRandomNumber(1, 9999));
    }
}