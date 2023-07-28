package com.krab51.webapp.services.demo;

import com.krab51.webapp.dto.TrapOwnerDto;
import com.krab51.webapp.mappers.ObjectMapper;
import com.krab51.webapp.repositories.TrapOwnerRepository;
import com.krab51.webapp.services.TrapOwnerService;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import static com.krab51.webapp.utilities.demo.RandomUtilites.getRandomNumber;

@Service
@Profile("demo")
public class TrapOwnerServiceDemo extends TrapOwnerService {

    @Autowired
    public TrapOwnerServiceDemo(TrapOwnerRepository trapOwnerRepository, ObjectMapper objectMapper) {
        super(trapOwnerRepository, objectMapper);
    }

    @PostConstruct
    public void setUp() {
        saveTrapOwner("ООО Рога и Копыта");
        saveTrapOwner("ООО Кортик");
    }

    public TrapOwnerDto getRandomTrapOwner() {
        return findById(getRandomNumber(1, trapOwnerRepository.count())).orElseThrow();
    }

    void saveTrapOwner(String name) {
        TrapOwnerDto trapOwnerDto = new TrapOwnerDto();

        trapOwnerDto.name = name;

        save(trapOwnerDto);
    }
}