package com.xpertgroup.cats.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * Configuration class for WebClient to connect to external APIs
 */
@Configuration
public class WebClientConfig {
    
    @Value("${cats.api.base-url}")
    private String catsApiBaseUrl;
    
    @Value("${cats.api.key}")
    private String catsApiKey;
    
    /**
     * Creates WebClient bean for Cat API
     * @return configured WebClient instance
     */
    @Bean("catApiWebClient")
    public WebClient catApiWebClient() {
        return WebClient.builder()
                .baseUrl(catsApiBaseUrl)
                .defaultHeader("x-api-key", catsApiKey)
                .defaultHeader("Content-Type", "application/json")
                .build();
    }
}