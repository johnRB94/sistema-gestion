package com.escuelaflorece.sistema_gestion.repository;
import com.escuelaflorece.sistema_gestion.model.Docente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DocenteRepository extends JpaRepository<Docente, Long> {}