package com.escuelaflorece.sistema_gestion.repository;
import com.escuelaflorece.sistema_gestion.model.Estudiante;
import org.springframework.data.jpa.repository.JpaRepository;
public interface EstudianteRepository extends JpaRepository<Estudiante, Long> {
}