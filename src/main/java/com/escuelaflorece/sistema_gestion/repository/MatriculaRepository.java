package com.escuelaflorece.sistema_gestion.repository;

import com.escuelaflorece.sistema_gestion.model.Matricula;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface MatriculaRepository extends JpaRepository<Matricula, Integer> {
    List<Matricula> findByEstudianteId(Long estudianteId);
    List<Matricula> findByAnioAcademico(Integer anio);
}