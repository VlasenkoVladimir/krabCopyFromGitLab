package com.krab51.webapp.configuration;

import com.krab51.webapp.services.userdetails.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import java.util.List;

@Configuration
public class WebSecurityConfig {
    private final CustomUserDetailsService customUserDetailsService;

    private final List<String> RESOURCES_WHITE_LIST = List.of(
            "/static/**",
            "/webjars/**",
            "/v3/api-docs/**",
            "/login",
            "/robots.txt"
    );

    private final List<String> USERS_PERMISSION_LIST = List.of(
            "/clients/**",
            "/kraborder/**",
            "/trapowners/**",
            "/traps/**",
            "/report/**",
            "/operator/**",
            "/h2-console/**"
    );

//    List<String> USERS_WHITE_LIST = List.of("/login");
//    List<String> USERS_REST_WHITE_LIST = List.of("/operators/auth");

    public static final String ADMIN = "ADMIN";
    public static final String OPERATOR = "OPERATOR";

    public WebSecurityConfig(CustomUserDetailsService customUserDetailsService) {
        this.customUserDetailsService = customUserDetailsService;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .headers().frameOptions().disable().and()
                .cors().disable()
                .csrf().disable()
                //Настройка http-запросов - кому/куда можно/нельзя
                .authorizeHttpRequests((requests) -> requests
                        .requestMatchers(RESOURCES_WHITE_LIST.toArray(String[]::new)).permitAll()
                        .requestMatchers(USERS_PERMISSION_LIST.toArray(String[]::new)).hasAnyRole(ADMIN,OPERATOR)
                        .anyRequest().authenticated()
                )
                //Настройка для входа в систему
                .formLogin((form) -> form
                        .loginPage("/login")
                        //Перенаправление на главную страницу после успеха
                        .defaultSuccessUrl("/").permitAll()
                )
                .logout((logout) -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login")
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID")
                        .permitAll()
                        .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                ).build();
    }

    @Autowired
    protected void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(customUserDetailsService).passwordEncoder(new BCryptPasswordEncoder());
    }

}