package com.escuelaflorece.sistema_gestion.controller;

import com.escuelaflorece.sistema_gestion.dto.LoginRequest;
import com.escuelaflorece.sistema_gestion.dto.TokenResponse;
import com.escuelaflorece.sistema_gestion.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                    request.getEmail(),
                    request.getPassword()
                )
            );

            UserDetails user = (UserDetails) authentication.getPrincipal();

            // Separar roles y permisos
            List<String> roles = user.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .filter(a -> a.startsWith("ROLE_"))
                    .map(a -> a.replace("ROLE_", ""))
                    .distinct()
                    .toList();

            List<String> permisos = user.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .filter(a -> !a.startsWith("ROLE_"))
                    .distinct()
                    .toList();

            String token = jwtUtil.createToken(user.getUsername(), roles, permisos);

            return ResponseEntity.ok(new TokenResponse(token, user.getUsername(), roles, permisos));

        } catch (BadCredentialsException ex) {
            return ResponseEntity.status(401).body("Credenciales inválidas");
        }
    }
}