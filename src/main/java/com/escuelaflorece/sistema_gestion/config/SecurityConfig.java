package com.escuelaflorece.sistema_gestion.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf
                .ignoringRequestMatchers("/api/**")  // ✅ Thunder Client no envía CSRF
            )
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/login", "/css/**", "/js/**", "/images/**").permitAll()
                .requestMatchers("/api/usuarios/registrar").permitAll()  // ✅ público para crear el primer admin
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
            .formLogin(form -> form
                .loginPage("/login")
                .loginProcessingUrl("/login")
                .defaultSuccessUrl("/dashboard", true)
                .failureUrl("/login?error=true")
                .permitAll()
            )
            .logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/login?logout=true")
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID")
                .permitAll()
            )
            .exceptionHandling(ex -> ex
                .accessDeniedPage("/login?denied=true")
            );

        return http.build();
    }
}