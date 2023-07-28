package com.krab51.webapp.configuration.dev;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.thymeleaf.spring6.SpringTemplateEngine;
import org.thymeleaf.spring6.templateresolver.SpringResourceTemplateResolver;

@Configuration
@Profile("dev")
public class TemplatesDevelopmentConfiguration {

    @Bean
    public SpringResourceTemplateResolver getTemplateResolver() {
        SpringResourceTemplateResolver resolver = new SpringResourceTemplateResolver();

        resolver.setSuffix(".html");
        resolver.setTemplateMode("HTML");
        resolver.setCharacterEncoding("UTF-8");
        resolver.setPrefix("file:src/main/resources/templates/");
        resolver.setCacheable(false);

        return resolver;
    }

    @Bean
    public SpringTemplateEngine getTemplateEngine() {
        SpringTemplateEngine templateEngine = new SpringTemplateEngine();

        templateEngine.setTemplateResolver(getTemplateResolver());
        //templateEngine.setAdditionalDialects(new HashSet<>(singletonList(new SpringSecurityDialect())));
        templateEngine.setEnableSpringELCompiler(false);

        return templateEngine;
    }
}