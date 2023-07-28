package com.krab51.webapp.services.demo;

import com.krab51.webapp.dto.OperatorDto;
import com.krab51.webapp.mappers.ObjectMapper;
import com.krab51.webapp.repositories.OperatorRepository;
import com.krab51.webapp.services.OperatorService;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Service
@Profile("demo")
public class OperatorServiceDemo extends OperatorService {

    @Autowired
    public OperatorServiceDemo(OperatorRepository operatorRepository, ObjectMapper objectMapper) {
        super(operatorRepository, objectMapper);
    }

    @PostConstruct
    public void setUp() {
        saveOperator("Василий Зайцев");
        saveOperator("Иван Кукуйцев");
        saveOperator("Иван Сусанин");
        saveOperator("Царь Морской");
    }

    void saveOperator(String name) {
        OperatorDto operatorDto = new OperatorDto();

        operatorDto.userName = name;
        operatorDto.password = "123";

        save(operatorDto);
    }
}