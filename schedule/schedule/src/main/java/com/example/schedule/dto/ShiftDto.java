package com.example.schedule.dto;

import com.example.schedule.entity.Employee;
import com.example.schedule.entity.enums.ShiftStatus;
import com.example.schedule.entity.enums.DayType;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZonedDateTime;

@Data
public class ShiftDto {

    private Long id;
    private LocalDate shiftDate;
    private LocalTime startTime;
    private LocalTime endTime;
    private ShiftStatus status;
    private DayType dayType;
    private Long employeeId;      // Reference to Employee
    private Long shiftPatternId;  // Reference to ShiftPattern
    private String notes;
    private ZonedDateTime createdAt;
    private ZonedDateTime updatedAt;
    private Employee createdBy;
}
