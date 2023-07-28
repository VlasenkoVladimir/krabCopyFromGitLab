package com.krab51.webapp;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@OpenAPIDefinition(info = @Info(title = "Krab 51 API"))
public class LicenseApplication {

    public static void main(String[] args) {
        SpringApplication.run(LicenseApplication.class, args);
    }
}