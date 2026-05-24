package com.escuelaflorece.sistema_gestion.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "usuarios")
@Data
@NoArgsConstructor
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;
    private String apellido;
    private String email;

    @Column(name = "password")  // ✅ Corregido
    private String contrasena;

    @Column(name = "rol_id")
    private Integer rol;

    @Column(name = "estado")
    private String estado;

    // Getters manuales
    public String getEmail() { return email; }
    public String getContrasena() { return contrasena; }
    public Integer getRol() { return rol; }
}