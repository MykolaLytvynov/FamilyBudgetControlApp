package com.mykola.familybudgetcontrolapp.api.dto.jwt;

import lombok.Data;

@Data
public class LoginRequest {
    private String username;
    private String password;
}
