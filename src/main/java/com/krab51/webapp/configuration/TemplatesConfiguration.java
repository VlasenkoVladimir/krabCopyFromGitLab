package com.krab51.webapp.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.thymeleaf.spring6.SpringTemplateEngine;
import org.thymeleaf.spring6.templateresolver.SpringResourceTemplateResolver;

@Configuration
@Profile("!dev")
public class TemplatesConfiguration {

    @Bean
    public SpringResourceTemplateResolver getTemplateResolver() {
        SpringResourceTemplateResolver resolver = new SpringResourceTemplateResolver();

        resolver.setSuffix(".html");
        resolver.setTemplateMode("HTML");
        resolver.setCharacterEncoding("UTF-8");
        resolver.setPrefix("classpath:/templates/");
        resolver.setCacheable(true);

        return resolver;
    }

    @Bean
    public SpringTemplateEngine getTemplateEngine() {
        SpringTemplateEngine templateEngine = new SpringTemplateEngine();

        templateEngine.setTemplateResolver(getTemplateResolver());
        //templateEngine.setAdditionalDialects(new HashSet<>(singletonList(new SpringSecurityDialect())));
        templateEngine.setEnableSpringELCompiler(true);

        return templateEngine;
    }
}