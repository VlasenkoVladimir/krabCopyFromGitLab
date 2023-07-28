package com.krab51.webapp.services.demo;

import com.krab51.webapp.dto.CompanyDto;
import com.krab51.webapp.mappers.ObjectMapper;
import com.krab51.webapp.repositories.CompanyRepository;
import com.krab51.webapp.services.CompanyService;
import jakarta.annotation.PostConstruct;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

import static com.krab51.webapp.utilities.demo.RandomUtilites.getRandomNumber;

@Service
@Profile("demo")
public class CompanyServiceDemo extends CompanyService {

    public CompanyServiceDemo(CompanyRepository companyRepository,
                              ObjectMapper objectMapper) {
        super(companyRepository, objectMapper);
    }

    @PostConstruct
    public void setUp() {
        LocalDate now = LocalDate.now();

        saveCompany(1L, now.minusYears(getRandomNumber(1, 3)));
    }

    void saveCompany(Long id, LocalDate regDate) {
        CompanyDto companyDto = new CompanyDto();

        companyDto.id = id;
        companyDto.companyName = "ООО \"Вкусные крабы\"";
        companyDto.companyRegDate = regDate;
        companyDto.companyAddress = "ИНН 1234567890, 183038 г. Мурманск пр-т Ленина д. 1 оф. 10 тел +7(800) 123-45-67";
        companyDto.fishingArea = "Рыболовный участок № 455: Баренцево море, бухта Долгая щель- бухта Малонемецкая Западная ( в соответствии с Перечнем рыболовных участков Мурманской области, утверждённым постановлением Правительства Мурманской области от 30.12.2019 г. №616-ПП) в границах с координатами системы WGS- 84 Ш= 69°44'36\", Д= 31°13'18\" Ш=69°45'26\", Д= 31°13'42\" Ш= 69°45'09\", Д= 31°17'42\" Ш= 69°43'11\", Д= 31°22'59\", Ш= 69°42'45\", Д= 31°21'39";
        companyDto.miningPermit = "Обьект лова:Камчатский краб (разрешение на добычу ВБР № 51 2022 06 0240 от \"11\" августа 2022 г.";

        save(companyDto);
    }
}
