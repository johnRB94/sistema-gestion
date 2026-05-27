package com.escuelaflorece.sistema_gestion.config;

import com.escuelaflorece.sistema_gestion.security.JwtAuthFilter;
import com.escuelaflorece.sistema_gestion.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private JwtAuthFilter jwtAuthFilter;

    @Bean
    public DaoAuthenticationProvider authenticationProvider(PasswordEncoder passwordEncoder) {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(usuarioService);
        provider.setPasswordEncoder(passwordEncoder);
        return provider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config)
            throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .authenticationProvider(authenticationProvider(null)) // Spring inyectará automáticamente el provider
            
            // CSRF: deshabilitado para /auth/** y /api/**
            .csrf(csrf -> csrf
                .ignoringRequestMatchers("/auth/**", "/api/**")
            )

            .authorizeHttpRequests(auth -> auth
                // Público
                .requestMatchers("/login", "/auth/**",
                                 "/css/**", "/js/**", "/images/**").permitAll()

                // API protegida por permisos JWT
                .requestMatchers("GET", "/api/estudiantes/**").hasAuthority("ESTUDIANTE_LEER")
                .requestMatchers("POST", "/api/estudiantes/**").hasAuthority("ESTUDIANTE_CREAR")
                .requestMatchers("PUT", "/api/estudiantes/**").hasAuthority("ESTUDIANTE_ACTUALIZAR")
                .requestMatchers("DELETE", "/api/estudiantes/**").hasAuthority("ESTUDIANTE_ELIMINAR")
                .requestMatchers("/api/usuarios/**").hasAuthority("USUARIO_LEER")
                .requestMatchers("/api/docentes/**").hasAuthority("USUARIO_LEER")
                .requestMatchers("/api/notas/**").hasAuthority("NOTA_REGISTRAR")
                .requestMatchers("/api/asistencias/**").hasAuthority("ASISTENCIA_REGISTRAR")
                .requestMatchers("/api/pagos/**").hasAuthority("USUARIO_LEER")
                .requestMatchers("/api/matriculas/**").hasAuthority("USUARIO_LEER")

                // Vistas web por rol
                .requestMatchers("/usuarios/**").hasRole("ADMIN")
                .requestMatchers("/pagos/**").hasRole("ADMIN")
                .requestMatchers("/matriculas/**").hasRole("ADMIN")
                .requestMatchers("/docentes/**").hasRole("ADMIN")
                .requestMatchers("/estudiantes/**").hasAnyRole("ADMIN", "DOCENTE")
                .requestMatchers("/asistencias/**").hasAnyRole("ADMIN", "DOCENTE")
                .requestMatchers("/notas/**").hasAnyRole("ADMIN", "DOCENTE")
                .requestMatchers("/dashboard").hasAnyRole("ADMIN", "DOCENTE")

                .anyRequest().authenticated()
            )

            // Login web (navegador)
            .formLogin(form -> form
                .loginPage("/login")
                .loginProcessingUrl("/login")
                .defaultSuccessUrl("/dashboard", true)
                .failureUrl("/login?error=true")
                .permitAll()
            )

            // Logout web
            .logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/login?logout=true")
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID")
                .permitAll()
            )

            .exceptionHandling(ex -> ex
                .accessDeniedPage("/login?denied=true")
            )

            // Agregar filtro JWT antes del filtro de autenticación
            .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}