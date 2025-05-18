package com.app.FlyWise.controller;

import com.app.FlyWise.dto.FlightDto;
import com.app.FlyWise.service.AmadeusService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/amadeus")
@RequiredArgsConstructor
public class AmadeusController {

    private final AmadeusService amadeusService;

    @GetMapping("/search")
    public ResponseEntity<List<FlightDto>> searchFlights(
            @RequestParam String origin,
            @RequestParam String destination,
            @RequestParam String date
    ) {
        List<FlightDto> flights = amadeusService.searchFlights(origin, destination, date);
        return ResponseEntity.ok(flights);
    }
}
