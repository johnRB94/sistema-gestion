package com.escuelaflorece.sistema_gestion.repository;

import com.escuelaflorece.sistema_gestion.model.Nota;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface NotaRepository extends JpaRepository<Nota, Integer> {
    List<Nota> findByEstudianteId(Long estudianteId);
    List<Nota> findByCursoId(Integer cursoId);
}