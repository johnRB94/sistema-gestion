package com.escuelaflorece.sistema_gestion.repository;

import com.escuelaflorece.sistema_gestion.model.Asistencia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AsistenciaRepository extends JpaRepository<Asistencia, Integer> {
    // Spring Boot se encarga del SQL (SELECT, INSERT, UPDATE, DELETE)
}