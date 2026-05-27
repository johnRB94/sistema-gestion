package com.escuelaflorece.sistema_gestion.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.util.List;

@Data
@AllArgsConstructor
public class TokenResponse {
    private String tokenType = "Bearer";
    private String token;
    private String email;
    private List<String> roles;
    private List<String> permisos;

    public TokenResponse(String token, String email, List<String> roles, List<String> permisos) {
        this.token = token;
        this.email = email;
        this.roles = roles;
        this.permisos = permisos;
    }
}