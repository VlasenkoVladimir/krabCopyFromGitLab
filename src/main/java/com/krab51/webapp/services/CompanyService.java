package com.krab51.webapp.services;

import com.krab51.webapp.domain.Company;
import com.krab51.webapp.dto.CompanyDto;
import com.krab51.webapp.mappers.ObjectMapper;
import com.krab51.webapp.repositories.CompanyRepository;
import org.slf4j.Logger;
import org.springframework.context.annotation.Profile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static org.slf4j.LoggerFactory.getLogger;
import static org.springframework.data.domain.Sort.Direction.DESC;

@Service
@Profile("default")
public class CompanyService {
    private final Logger logger = getLogger(Company.class);
    protected final CompanyRepository companyRepository;
    private final ObjectMapper objectMapper;
    private final Sort SORT = Sort.by(DESC, "id");

    public CompanyService(CompanyRepository companyRepository, ObjectMapper objectMapper) {
        this.companyRepository = companyRepository;
        this.objectMapper = objectMapper;
    }

    public void save(CompanyDto companyDto) {
        logger.info("Calling save at company service with CompanyDto: {}", companyDto);

        companyRepository.save(objectMapper.companyDtoToCompany(companyDto));
    }

    public void deleteById(Long id) {
        logger.info("Calling deleteById at company service with id: {}", id);

        companyRepository.deleteById(id);
    }

    public Optional<CompanyDto> findById(Long id) {
        logger.info("Calling findById at company service with id: {}", id);

        return companyRepository.findById(id).map(objectMapper::companyToCompanyDto);
    }

    public Page<CompanyDto> findAll(int page, int size) {
        logger.info("Calling findAll at company service with page {} and size {}", page, size);

        return companyRepository.findAll(PageRequest.of(page, size, SORT)).map(objectMapper::companyToCompanyDto);
    }
}
