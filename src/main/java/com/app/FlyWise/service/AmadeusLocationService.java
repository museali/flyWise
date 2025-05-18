package com.app.FlyWise.service;

import com.app.FlyWise.config.AmadeusConfig;
import com.app.FlyWise.dto.LocationDto;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Service
@RequiredArgsConstructor
public class AmadeusLocationService {

    private final AmadeusConfig config;
    private final AmadeusService amadeusService;
    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();

    public List<LocationDto> searchLocations(String keyword) {
        String token = amadeusService.getAccessToken(); // Reuse existing token logic

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

        String url = String.format(
                "https://test.api.amadeus.com/v1/reference-data/locations?subType=AIRPORT,CITY&keyword=%s&view=FULL",
                keyword
        );

        HttpEntity<Void> entity = new HttpEntity<>(headers);
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);

        return parseLocations(response.getBody());
    }

    private List<LocationDto> parseLocations(String json) {
        List<LocationDto> results = new ArrayList<>();

        try {
            JsonNode root = objectMapper.readTree(json);
            JsonNode data = root.get("data");

            if (data != null && data.isArray()) {
                for (JsonNode loc : data) {
                    String iataCode = loc.get("iataCode").asText();
                    String cityName = loc.get("address").get("cityName").asText();
                    String country = loc.get("address").get("countryName").asText();
                    String name = loc.get("name").asText();

                    results.add(new LocationDto(iataCode, cityName, country, name));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return results;
    }
}
