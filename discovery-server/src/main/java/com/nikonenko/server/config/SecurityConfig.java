package com.nikonenko.server.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;


@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Value("${eureka.username}")
    private String username;

    @Value("${eureka.password}")
    private String password;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder authenticationManagerBuilder = http.getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder
                .inMemoryAuthentication()
                .passwordEncoder(NoOpPasswordEncoder.getInstance())
                .withUser(username).password(password)
                .authorities("USER");
        AuthenticationManager authenticationManager = authenticationManagerBuilder.build();

        http
                .csrf(AbstractHttpConfigurer::disable)
                .authenticationManager(authenticationManager)
                .authorizeHttpRequests(authorizationManagerRequestMatcherRegistry ->
                {
                    try {
                        authorizationManagerRequestMatcherRegistry.anyRequest().authenticated()
                                .and()
                                .httpBasic();
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                })
        ;

        return http.build();
    }
}
