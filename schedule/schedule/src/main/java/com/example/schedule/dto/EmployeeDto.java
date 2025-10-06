package com.example.schedule.dto;

import com.example.schedule.entity.enums.Role;
import lombok.Data;
import java.time.ZonedDateTime;

@Data
public class EmployeeDto {

    private Long id;
    private String name;
    private String surname;
    private String email;
    private String phone;
    private Double hourlyRate;
    private Integer weeklyHours;
    private Boolean isActive;
    private Role role;
    private ZonedDateTime createdAt;
    private ZonedDateTime updatedAt;
}
