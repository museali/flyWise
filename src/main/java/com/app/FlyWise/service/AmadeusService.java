package com.app.FlyWise.service;

import com.app.FlyWise.config.AmadeusConfig;
import com.app.FlyWise.dto.FlightDto;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AmadeusService {

    private final AmadeusConfig config;
    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();

    private String accessToken;

    private String getAccessToken() {
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

        if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
            accessToken = (String) response.getBody().get("access_token");
            return accessToken;
        }

        throw new RuntimeException("Failed to retrieve access token");
    }

    public List<FlightDto> searchFlights(String origin, String destination, String date) {
        String token = getAccessToken();

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

        String url = String.format(
                "%s?originLocationCode=%s&destinationLocationCode=%s&departureDate=%s&adults=1&currencyCode=GBP&max=5",
                config.getSearchUrl(), origin, destination, date
        );

        HttpEntity<Void> request = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, request, String.class);

        return parseFlights(response.getBody());
    }

    private List<FlightDto> parseFlights(String json) {
        List<FlightDto> result = new ArrayList<>();

        try {
            JsonNode root = objectMapper.readTree(json);
            JsonNode data = root.get("data");

            if (data != null && data.isArray()) {
                for (JsonNode offer : data) {
                    JsonNode itinerary = offer.get("itineraries").get(0);
                    JsonNode segment = itinerary.get("segments").get(0);
                    JsonNode departure = segment.get("departure");
                    JsonNode arrival = segment.get("arrival");

                    String origin = departure.get("iataCode").asText();
                    String destination = arrival.get("iataCode").asText();
                    String departureDate = departure.get("at").asText();
                    String duration = itinerary.get("duration").asText();

                    String airline = segment.get("carrierCode").asText();
                    String flightNumber = segment.get("number").asText();
                    double price = offer.get("price").get("total").asDouble();

                    result.add(new FlightDto(
                            origin, destination, departureDate, airline, flightNumber, duration, price
                    ));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }
}
