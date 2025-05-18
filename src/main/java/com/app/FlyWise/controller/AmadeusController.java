package com.app.FlyWise.controller;

import com.app.FlyWise.service.AmadeusService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/amadeus")
@RequiredArgsConstructor
public class AmadeusController {

    private final AmadeusService amadeusService;

    @GetMapping("/search")
    public ResponseEntity<String> searchFlights(
            @RequestParam String origin,
            @RequestParam String destination,
            @RequestParam String date
    ) {
        return amadeusService.searchFlights(origin, destination, date);
    }
}
