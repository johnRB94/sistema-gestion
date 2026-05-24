package com.escuelaflorece.sistema_gestion.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "notas")
@Data
@NoArgsConstructor
public class Nota {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "estudiante_id", nullable = false)
    private Long estudianteId;

    @Column(name = "curso_id", nullable = false)
    private Integer cursoId;

    @Column(name = "matricula_id", nullable = false)
    private Integer matriculaId;

    @Column(nullable = false)
    private Double nota;

    @Column(name = "periodo_id", nullable = false)
    private Integer periodoId;
}