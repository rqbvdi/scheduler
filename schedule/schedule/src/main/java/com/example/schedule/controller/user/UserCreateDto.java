package com.example.schedule.controller.user;

import lombok.Data;

@Data
public class UserCreateDto {
    private String username;
    private String email;
    private String password;
}
