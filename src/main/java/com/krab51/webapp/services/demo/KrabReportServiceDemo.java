package com.krab51.webapp.services.demo;

import com.krab51.webapp.domain.KrabOrder;
import com.krab51.webapp.domain.KrabReport;
import com.krab51.webapp.mappers.ObjectMapper;
import com.krab51.webapp.repositories.KrabOrderRepository;
import com.krab51.webapp.repositories.KrabReportRepository;
import com.krab51.webapp.services.KrabOrderService;
import com.krab51.webapp.services.KrabReportService;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

import static com.krab51.webapp.utilities.demo.RandomUtilites.getRandomNumber;

@Service
@Profile("demo")
@DependsOn({"krabOrderServiceDemo", "trapServiceDemo"})
public class KrabReportServiceDemo extends KrabReportService {

    private final KrabOrderRepository krabOrderRepository;

    @Autowired
    public KrabReportServiceDemo(KrabReportRepository krabReportRepository,
                                 KrabOrderRepository krabOrderRepository,
                                 ObjectMapper objectMapper,
                                 KrabOrderService krabOrderService) {
        super(krabReportRepository, objectMapper, krabOrderService);
        this.krabOrderRepository = krabOrderRepository;
    }

    @PostConstruct
    public void setUp() {
        krabOrderRepository.findAll().forEach(krabOrder -> {
            if (krabOrder.endDate != null) saveKrabReport(krabOrder);
        });
    }

    void saveKrabReport(KrabOrder krabOrder) {
        KrabReport krabReport = new KrabReport();

        krabReport.krabOrder = krabOrder;
        krabReport.startDate = krabOrder.beginDate.plusHours(1);
        krabReport.endDate = krabOrder.endDate;
        krabReport.actuallyCnt = (int) getRandomNumber(0, krabOrder.enlistedCnt);
        krabReport.releasedCnt = (int) getRandomNumber(0, 10);
        krabReport.actuallyKgs = BigDecimal.valueOf(krabReport.actuallyCnt * getRandomNumber(1, 3));

        krabReportRepository.save(krabReport);
    }
}