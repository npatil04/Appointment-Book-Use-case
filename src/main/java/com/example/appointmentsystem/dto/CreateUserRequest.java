package com.example.appointmentsystem.dto;


import com.example.appointmentsystem.enums.Role;

import lombok.Data;

@Data
public class CreateUserRequest {

    private String username;
    private String password;
    private String email;
    private Role role;
    private String mobilenumber;
}
