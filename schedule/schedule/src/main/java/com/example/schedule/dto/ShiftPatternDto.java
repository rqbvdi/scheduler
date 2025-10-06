package com.example.schedule.dto;

import lombok.Data;

import java.time.LocalTime;

@Data
public class ShiftPatternDto {
    private Long id;
    private String name;
    private Integer workDays;
    private Integer restDays;
    private LocalTime defaultStart;
    private LocalTime defaultEnd;
}
