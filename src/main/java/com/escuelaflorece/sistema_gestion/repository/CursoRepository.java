package com.escuelaflorece.sistema_gestion.repository;
import com.escuelaflorece.sistema_gestion.model.Curso;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CursoRepository extends JpaRepository<Curso, Integer> {
}