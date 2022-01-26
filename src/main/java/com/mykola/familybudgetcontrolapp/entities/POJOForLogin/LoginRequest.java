package com.mykola.familybudgetcontrolapp.entities.POJOForLogin;

import lombok.Data;

@Data
public class LoginRequest {
    private String username;
    private String password;
}
