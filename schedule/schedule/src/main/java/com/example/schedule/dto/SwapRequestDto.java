package com.example.schedule.dto;

import com.example.schedule.entity.enums.RequestStatus;
import lombok.Data;

import java.time.ZonedDateTime;

@Data
public class SwapRequestDto {

    private Long id;
    private Long requestedShiftId;
    private Long offeredShiftId;
    private Long employeeFromId;
    private Long employeeToId;
    private RequestStatus status;
    private String reason;
    private Long approvedById;
    private ZonedDateTime approvedAt;
    private ZonedDateTime createdAt;
    private ZonedDateTime updatedAt;
}
