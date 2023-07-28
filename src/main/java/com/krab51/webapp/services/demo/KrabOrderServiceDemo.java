package com.krab51.webapp.services.demo;

import com.krab51.webapp.domain.Company;
import com.krab51.webapp.domain.Trap;
import com.krab51.webapp.dto.KrabOrderDto;
import com.krab51.webapp.mappers.ObjectMapper;
import com.krab51.webapp.repositories.CompanyRepository;
import com.krab51.webapp.repositories.KrabOrderRepository;
import com.krab51.webapp.repositories.KrabReportRepository;
import com.krab51.webapp.repositories.TrapRepository;
import com.krab51.webapp.services.KrabOrderService;
import jakarta.annotation.PostConstruct;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import static com.krab51.webapp.utilities.demo.RandomUtilites.getRandomNumber;

@Service
@Profile("demo")
@DependsOn({"trapServiceDemo", "companyServiceDemo", "clientServiceDemo"})
public class KrabOrderServiceDemo extends KrabOrderService {
    private final ClientServiceDemo clientServiceDemo;
    private final TrapRepository trapRepository;
    private List<Trap> traps;
    private Company company;
    private final CompanyRepository companyRepository;

    public KrabOrderServiceDemo(KrabOrderRepository krabOrderRepository,
                                ObjectMapper objectMapper,
                                TrapRepository trapRepository,
                                ClientServiceDemo clientServiceDemo,
                                CompanyRepository companyRepository,
                                KrabReportRepository krabReportRepository) {
        super(krabOrderRepository, objectMapper, trapRepository, krabReportRepository);
        this.clientServiceDemo = clientServiceDemo;
        this.trapRepository = trapRepository;
        this.companyRepository = companyRepository;
    }

    @PostConstruct
    public void setUp() {
        LocalDateTime now = LocalDateTime.now();
        LocalDate nowLocalDate = now.toLocalDate();

        traps = trapRepository.findAll();
        company = companyRepository.findById(1L).orElse(null);

        // Генерируем закрытые заявки
        for (int count = 113; count > 20; count--)
            saveOrder(nowLocalDate.minusDays(count), now.minusDays(count).plusHours(4),
                    now.minusDays(count - getRandomNumber(1, 3)).minusHours(1));

        // Генерируем ошибочные заявки
        saveOrder(nowLocalDate.minusDays(10), now.minusDays(10).plusHours(3), now.minusDays(5));
        saveOrder(nowLocalDate.minusDays(7), now.minusDays(7).plusHours(2), null);

        // Генерируем заявки, до завершения которых осталось меньше суток
        saveOrder(nowLocalDate.minusDays(3), now.minusDays(3).plusHours(2), null);
        saveOrder(now.toLocalDate().minusDays(3), now.minusDays(3).plusHours(12), null);

        // Генерируем заявки в работе
        saveOrder(nowLocalDate.minusDays(2), now.minusDays(2).plusHours(12), null);
        saveOrder(nowLocalDate.minusDays(1), now.minusDays(1).plusHours(2), null);
        saveOrder(nowLocalDate.minusDays(1), now.minusDays(1).plusHours(10), null);
    }

    void saveOrder(LocalDate regDate, LocalDateTime beginDate, LocalDateTime endDate) {
        KrabOrderDto krabOrderDto = new KrabOrderDto();

        krabOrderDto.regDate = regDate;
        krabOrderDto.client = clientServiceDemo.getRandomClient();
        krabOrderDto.enlistedCnt = (int) getRandomNumber(1, 3);
        krabOrderDto.price = new BigDecimal("1500").multiply(new BigDecimal(krabOrderDto.enlistedCnt));
        krabOrderDto.beginDate = beginDate;
        krabOrderDto.endDate = endDate;
        krabOrderDto.expirationDaysNumber = 3;
        krabOrderDto.company = company;

        krabOrderDto.traps = Set.of(traps.get((int) getRandomNumber(0, traps.size() - 1)).regNumber);

        save(krabOrderDto);
    }
}