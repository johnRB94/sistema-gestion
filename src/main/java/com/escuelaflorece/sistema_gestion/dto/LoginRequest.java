package com.escuelaflorece.sistema_gestion.dto;

import lombok.Data;

@Data
public class LoginRequest {
    private String email;
    private String password;
}