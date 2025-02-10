package com.app.FlyWise.service;

import com.app.FlyWise.model.Holiday;
import com.app.FlyWise.repository.HolidayRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class HolidayService {
    private final HolidayRepository holidayRepository;

    public List<Holiday> getHolidaysInRange(LocalDate startDate, LocalDate endDate) {
        return holidayRepository.findByDateBetween(startDate, endDate);
    }

    public Holiday saveHoliday(Holiday holiday) {
        return holidayRepository.save(holiday);
    }
}
