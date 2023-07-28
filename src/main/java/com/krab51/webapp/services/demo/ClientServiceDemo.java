package com.krab51.webapp.services.demo;

import com.krab51.webapp.dto.ClientDto;
import com.krab51.webapp.mappers.ObjectMapper;
import com.krab51.webapp.repositories.ClientRepository;
import com.krab51.webapp.services.ClientService;
import com.krab51.webapp.utilities.demo.CitizenAddressUtil;
import com.krab51.webapp.utilities.demo.CitizenDocumentUtil;
import com.krab51.webapp.utilities.demo.CitizenNameUtil;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static com.krab51.webapp.utilities.demo.RandomUtilites.getRandomDate;
import static com.krab51.webapp.utilities.demo.RandomUtilites.getRandomNumber;
import static com.krab51.webapp.utilities.demo.RandomUtilites.getRandomPhoneNumber;

@Service
@Profile("demo")
public class ClientServiceDemo extends ClientService {
    private static final CitizenNameUtil nameUtil = new CitizenNameUtil();
    private static final CitizenAddressUtil addressUtil = new CitizenAddressUtil();
    private static final CitizenDocumentUtil documentUtil = new CitizenDocumentUtil();

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
    private static final long DOC_DATE_BEGIN = 2011L;
    private static final long DOC_DATE_END = 2019L;

    @Autowired
    public ClientServiceDemo(ClientRepository clientRepository, ObjectMapper objectMapper) {
        super(clientRepository, objectMapper);
    }

    @PostConstruct
    public void setUp() {
        for (int i = 0; i < 100; i++) saveClient();
    }

    public ClientDto getRandomClient() {
        return findById(getRandomNumber(1, clientRepository.count())).orElseThrow();
    }

    void saveClient() {
        CitizenNameUtil.CitizenName citizenName = nameUtil.generateRandomName(nameUtil.generateRandomGender());
        CitizenAddressUtil.KladrObject registryAddress = addressUtil.generateRandomAddress();
        CitizenDocumentUtil.Passport passport = documentUtil.generateRandomPassport(registryAddress.getRegionCode(),
                registryAddress.getOcatoCode(), getRandomDate(DOC_DATE_BEGIN, DOC_DATE_END));

        ClientDto clientDto = new ClientDto();

        clientDto.firstName = citizenName.first;
        clientDto.lastName = citizenName.last;
        clientDto.middleName = citizenName.middle;
        clientDto.registration = buildRegistration(registryAddress);
        clientDto.docType = "паспорт";
        clientDto.docNumber = passport.series + " " + passport.number;
        clientDto.docAuthority = passport.authority.name;
        clientDto.docDate = LocalDate.parse(passport.date, formatter);
        clientDto.phoneNumber = getRandomPhoneNumber();

        save(clientDto);
    }

    String buildRegistration(CitizenAddressUtil.KladrObject registryAddress) {
        return registryAddress.getRegionName() +
                " " +
                registryAddress.getDistrictName() +
                " " +
                registryAddress.getName();
    }
}