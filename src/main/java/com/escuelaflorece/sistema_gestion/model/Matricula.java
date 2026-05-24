package com.escuelaflorece.sistema_gestion.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Entity
@Table(name = "matriculas")
@Data
@NoArgsConstructor
public class Matricula {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "estudiante_id", nullable = false)
    private Long estudianteId;

    @Column(name = "grado_id", nullable = false)
    private Integer gradoId;

    @Column(nullable = false)
    private LocalDate fecha;

    @Column(nullable = false, length = 20)
    private String estado = "Activa"; // Activa, Pendiente, Cancelada

    @Column(name = "anio_academico", nullable = false)
    private Integer anioAcademico;
}