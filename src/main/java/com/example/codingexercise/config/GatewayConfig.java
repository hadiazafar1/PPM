package com.example.codingexercise.config;

import java.time.Duration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.support.BasicAuthenticationInterceptor;
import org.springframework.web.client.RestTemplate;

@Configuration
public class GatewayConfig {

    // RestTemplate bean with basic authentication and timeouts
    @Bean
    public RestTemplate restTemplate(
            RestTemplateBuilder builder,
            @Value("${app.http.connect-timeout-ms:2000}") int connectTimeoutMs,
            @Value("${app.http.read-timeout-ms:3000}") int readTimeoutMs,
            @Value("${app.http.basic-auth.username:}") String basicUser,
            @Value("${app.http.basic-auth.password:}") String basicPass) {

        RestTemplate rt = builder
                .setConnectTimeout(Duration.ofMillis(connectTimeoutMs))
                .setReadTimeout(Duration.ofMillis(readTimeoutMs))
                .build();

        // Only add Basic Auth if a username is provided
        if (basicUser != null && !basicUser.isBlank()) {
            rt.getInterceptors().add(new BasicAuthenticationInterceptor(basicUser, basicPass));
        }

        return rt;
    }
}
