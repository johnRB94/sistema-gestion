package com.escuelaflorece.sistema_gestion;

import org.springframework.boot.SpringApplication; // <-- ESTA ES LA QUE DA ERROR
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SistemaGestionApplication {
    public static void main(String[] args) {
        SpringApplication.run(SistemaGestionApplication.class, args);
    }
}