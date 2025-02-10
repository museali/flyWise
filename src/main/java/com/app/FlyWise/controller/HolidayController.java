package com.app.FlyWise.controller;

import com.app.FlyWise.model.Holiday;
import com.app.FlyWise.service.HolidayService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/holidays")
public class HolidayController {
    private final HolidayService holidayService;

    @Autowired
    public HolidayController(HolidayService holidayService) {
        this.holidayService = holidayService;
    }

    @GetMapping("/range")
    public ResponseEntity<List<Holiday>> getHolidaysInRange(
            @RequestParam String startDate,
            @RequestParam String endDate) {
        List<Holiday> holidays = holidayService.getHolidaysInRange(LocalDate.parse(startDate), LocalDate.parse(endDate));
        return ResponseEntity.ok(holidays);
    }

    @PostMapping
    public ResponseEntity<Holiday> addHoliday(@RequestBody Holiday holiday) {
        Holiday savedHoliday = holidayService.saveHoliday(holiday);
        return ResponseEntity.ok(savedHoliday);
    }
}