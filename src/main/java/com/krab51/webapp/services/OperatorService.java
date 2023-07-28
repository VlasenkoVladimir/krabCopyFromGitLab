package com.krab51.webapp.services;

import com.krab51.webapp.dto.OperatorDto;
import com.krab51.webapp.exceptions.BusinessException;
import com.krab51.webapp.mappers.ObjectMapper;
import com.krab51.webapp.repositories.OperatorRepository;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.regex.Pattern;

import static org.slf4j.LoggerFactory.getLogger;

@Service
@Profile("default")
public class OperatorService {
    private final Logger logger = getLogger(OperatorService.class);
    private final Pattern pattern = Pattern.compile("[а-яёА-ЯЁ ]+");
    protected final OperatorRepository operatorRepository;
    private final ObjectMapper objectMapper;

    @Autowired
    public OperatorService(OperatorRepository operatorRepository, ObjectMapper objectMapper) {
        this.operatorRepository = operatorRepository;
        this.objectMapper = objectMapper;
    }

    public void save(OperatorDto operatorDto) {
        logger.info("Calling save at operator service");

        if (!pattern.matcher(operatorDto.userName).matches()) {
            throw new BusinessException("Разрешается использовать только русские буквы");
        }

        operatorRepository.save(objectMapper.operatorDtoToOperator(operatorDto));
    }

    public void deleteById(Long id) {
        logger.info("Calling deleteById at operator service");

        operatorRepository.deleteById(id);
    }

    public Optional<OperatorDto> findById(Long id) {
        logger.info("Calling findById at operator service");

        return operatorRepository.findById(id).map(objectMapper::operatorToOperatorDto);
    }

    public Page<OperatorDto> findAll(int page, int size) {
        logger.info("Calling findAll at operator service");

        return operatorRepository.findAll(PageRequest.of(page, size)).map(objectMapper::operatorToOperatorDto);
    }
}