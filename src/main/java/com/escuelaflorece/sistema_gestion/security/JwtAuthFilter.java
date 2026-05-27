package com.escuelaflorece.sistema_gestion.security;

import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain)
            throws ServletException, IOException {
        try {
            String token = jwtUtil.resolveToken(request);
            if (token != null) {
                Claims claims = jwtUtil.parseClaims(token);
                if (jwtUtil.isTokenValid(claims)) {
                    String email = jwtUtil.getEmail(claims);
                    List<String> roles = jwtUtil.getRoles(claims);
                    List<String> permisos = jwtUtil.getPermisos(claims);

                    List<SimpleGrantedAuthority> authorities = new ArrayList<>();
                    for (String rol : roles) {
                        authorities.add(new SimpleGrantedAuthority("ROLE_" + rol));
                    }
                    for (String permiso : permisos) {
                        authorities.add(new SimpleGrantedAuthority(permiso));
                    }

                    UsernamePasswordAuthenticationToken auth =
                            new UsernamePasswordAuthenticationToken(email, null, authorities);
                    auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(auth);
                }
            }
        } catch (Exception ex) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Token inválido o expirado");
            return;
        }
        chain.doFilter(request, response);
    }

    // Solo aplica el filtro JWT a rutas /auth/** y /api/**
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getServletPath();
        return !path.startsWith("/auth/") && !path.startsWith("/api/");
    }
}