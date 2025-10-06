package com.example.schedule.dto;

import lombok.Data;
import java.time.ZonedDateTime;

@Data
public class AuthenticationDto {

    private Long id;
    private String username;
    private String role;
    private Boolean active;
    private ZonedDateTime createdAt;
    private ZonedDateTime updatedAt;

}
