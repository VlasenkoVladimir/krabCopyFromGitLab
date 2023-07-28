package com.krab51.webapp.configuration.dev;

import com.krab51.webapp.configuration.base.BaseWebMvcConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.lang.NonNull;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;

@Configuration
@Profile("dev")
public class WebMvcDevelopmentConfiguration extends BaseWebMvcConfiguration {

    @Override
    public void addResourceHandlers(@NonNull ResourceHandlerRegistry registry) {
        super.addResourceHandlers(registry);

        registry.addResourceHandler("/static/**")
                .addResourceLocations("file:src/main/resources/static/")
                .setCachePeriod(0);
    }
}