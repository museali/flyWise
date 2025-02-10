package com.app.FlyWise.service;

import com.app.FlyWise.model.Flight;
import com.app.FlyWise.repository.FlightRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FlightService {
    private final FlightRepository flightRepository;

    public List<Flight> searchFlights(String origin, String destination, LocalDate departureDate) {
        return flightRepository.findByOriginAndDestinationAndDepartureDate(origin, destination, departureDate);
    }

    public Flight saveFlight(Flight flight) {
        return flightRepository.save(flight);
    }
}
