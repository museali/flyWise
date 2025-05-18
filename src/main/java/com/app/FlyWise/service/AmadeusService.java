package com.app.FlyWise.service;

import com.app.FlyWise.config.AmadeusConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AmadeusService {

    private final AmadeusConfig config;
    private final RestTemplate restTemplate = new RestTemplate();

    private String accessToken;

    public String getAccessToken() {
        if (accessToken != null) return accessToken;

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> form = new LinkedMultiValueMap<>();
        form.add("grant_type", "client_credentials");
        form.add("client_id", config.getApiKey());
        form.add("client_secret", config.getApiSecret());

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(form, headers);

        ResponseEntity<Map> response = restTemplate.postForEntity(
                config.getTokenUrl(), request, Map.class
        );

        System.out.println("Token response: " + response);

        if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
            accessToken = (String) response.getBody().get("access_token");
            System.out.println("Access token: " + accessToken);
            return accessToken;
        }

        throw new RuntimeException("Failed to get Amadeus access token");
    }

    public ResponseEntity<String> searchFlights(String origin, String destination, String date) {
        String token = getAccessToken();

        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.setBearerAuth(token); // ✅ critical

        String url = String.format(
                "%s?originLocationCode=%s&destinationLocationCode=%s&departureDate=%s&adults=1",
                config.getSearchUrl(), origin, destination, date
        );

        System.out.println("Final search URL: " + url);
        System.out.println("Using token: " + token);

        HttpEntity<Void> request = new HttpEntity<>(headers);

        return restTemplate.exchange(url, HttpMethod.GET, request, String.class);
    }
}
