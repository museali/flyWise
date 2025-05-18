package com.app.FlyWise.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class FlightDto {
    private String origin;
    private String destination;
    private String departureDate;
    private String airline;
    private String flightNumber;
    private String duration;
    private double price;
}
