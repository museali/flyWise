package com.app.FlyWise.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LocationDto {
    private String iataCode;
    private String cityName;
    private String countryName;
    private String airportName;
}