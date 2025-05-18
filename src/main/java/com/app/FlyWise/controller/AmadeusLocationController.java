package com.app.FlyWise.controller;

import com.app.FlyWise.dto.LocationDto;
import com.app.FlyWise.service.AmadeusLocationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/amadeus")
@RequiredArgsConstructor
public class AmadeusLocationController {

    private final AmadeusLocationService locationService;

    @GetMapping("/locations")
    public ResponseEntity<List<LocationDto>> searchLocations(@RequestParam String query) {
        List<LocationDto> locations = locationService.searchLocations(query);
        return ResponseEntity.ok(locations);
    }
}
