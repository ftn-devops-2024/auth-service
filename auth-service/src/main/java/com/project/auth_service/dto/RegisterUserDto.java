package com.project.auth_service.dto;

import lombok.Data;

@Data
public class RegisterUserDto {

    private String name;

    private String surname;

    private String email;

    private String password;

    private String role;

}
