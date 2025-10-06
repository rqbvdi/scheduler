package com.example.schedule.dto;

import com.example.schedule.entity.enums.RequestStatus;
import lombok.Data;

import java.time.ZonedDateTime;

@Data
public class LeaveRequestDto {

    private Long id;
    private Long employeeId; // Reference to Employee by ID
    private ZonedDateTime start;
    private ZonedDateTime end;
    private String reason;
    private RequestStatus status;
    private Long approvedBy;
    private ZonedDateTime approvedAt;
    private ZonedDateTime createdAt;
}
