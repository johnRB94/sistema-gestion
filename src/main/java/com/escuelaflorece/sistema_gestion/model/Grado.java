package com.escuelaflorece.sistema_gestion.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "grados")
@Data
@NoArgsConstructor
public class Grado {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String nombre;

    @Column(name = "nivel_id", nullable = false)
    private Integer nivelId;

    @Column(length = 1)
    private String seccion;
}