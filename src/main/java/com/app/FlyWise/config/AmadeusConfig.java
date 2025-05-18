package com.app.FlyWise.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Getter
public class AmadeusConfig {

    @Value("${amadeus.api.key}")
    private String apiKey;

    @Value("${amadeus.api.secret}")
    private String apiSecret;

    @Value("${amadeus.api.token-url}")
    private String tokenUrl;

    @Value("${amadeus.api.search-url}")
    private String searchUrl;
}