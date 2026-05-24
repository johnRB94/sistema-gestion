package com.escuelaflorece.sistema_gestion.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDate;

@Entity
@Table(name = "asistencias")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Asistencia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "estudiante_id", nullable = false)
    private Long estudianteId;

    @Column(name = "curso_id", nullable = false)
    private Integer cursoId;

    @Column(nullable = false)
    private LocalDate fecha;

    @Column(nullable = false, length = 15)
    private String estado; // Presente, Ausente, Tardanza
}