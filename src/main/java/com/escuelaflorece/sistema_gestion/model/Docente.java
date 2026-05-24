package com.escuelaflorece.sistema_gestion.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Entity
@Table(name = "docentes")
@Data
@NoArgsConstructor
public class Docente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nombre;

    @Column(nullable = false)
    private String apellido;

    @Column(nullable = false, unique = true)
    private String email;

    private String especialidad1;
    private String especialidad2;

    @Column(name = "nivel_id", nullable = false)
    private Integer nivelId;

    private String telefono;

    @Column(name = "fecha_contratacion")
    private LocalDate fechaContratacion;

    @Column(name = "usuario_id")
    private Long usuarioId;
}