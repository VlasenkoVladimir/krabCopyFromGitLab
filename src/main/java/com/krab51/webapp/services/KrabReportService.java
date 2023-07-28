package com.krab51.webapp.services;

import com.krab51.webapp.domain.KrabReport;
import com.krab51.webapp.dto.KrabOrderDto;
import com.krab51.webapp.dto.KrabReportDto;
import com.krab51.webapp.exceptions.BusinessException;
import com.krab51.webapp.mappers.ObjectMapper;
import com.krab51.webapp.repositories.KrabReportRepository;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static org.slf4j.LoggerFactory.getLogger;

@Service
@Profile("default")
public class KrabReportService {
    private final Logger logger = getLogger(KrabReportService.class);
    protected final KrabReportRepository krabReportRepository;
    protected final KrabOrderService krabOrderService;
    private final ObjectMapper objectMapper;

    @Autowired
    public KrabReportService(KrabReportRepository krabReportRepository,
                             ObjectMapper objectMapper,
                             KrabOrderService krabOrderService) {
        this.krabReportRepository = krabReportRepository;
        this.objectMapper = objectMapper;
        this.krabOrderService = krabOrderService;
    }

    public void save(KrabReportDto krabReportDto) {
        logger.info("Calling save at report service");

        KrabOrderDto krabOrderDto = krabOrderService.findById(krabReportDto.krabOrder.id)
                .orElseThrow(() -> new BusinessException("Order with this id not found"));

        if (krabOrderDto.beginDate.isAfter(krabReportDto.startDate)) {
            throw new BusinessException("Not valid start catching date");
        }

        int actuallyCntSum = 0;
        List<KrabReport> krabReportList = krabReportRepository.findByKrabOrderId(krabReportDto.krabOrder.id);

        for (KrabReport report : krabReportList) {
            if (!report.id.equals(krabReportDto.id)) {
                actuallyCntSum += report.actuallyCnt;
            }
        }

        if (actuallyCntSum + krabReportDto.actuallyCnt > krabReportDto.krabOrder.enlistedCnt) {
            throw new BusinessException("Too many krabs");
        }

        krabReportRepository.save(objectMapper.krabReportDtoToKrabReport(krabReportDto));
    }

    public void deleteById(Long id) {
        logger.info("Calling deleteById at report service");

        krabReportRepository.deleteById(id);
    }

    public Optional<KrabReportDto> findById(Long id) {
        logger.info("Calling findById at report service");

        return krabReportRepository.findById(id).map(objectMapper::krabReportToKrabReportDto);
    }

    public Page<KrabReportDto> findAll(int page, int size) {
        logger.info("Calling findAll at report service");

        return krabReportRepository.findAll(PageRequest.of(page, size)).map(objectMapper::krabReportToKrabReportDto);
    }
}