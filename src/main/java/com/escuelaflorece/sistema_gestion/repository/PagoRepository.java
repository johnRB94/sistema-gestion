package com.escuelaflorece.sistema_gestion.repository;

import com.escuelaflorece.sistema_gestion.model.Pago;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PagoRepository extends JpaRepository<Pago, Integer> {
    // Métodos CRUD listos para ser explotados por los servicios web
}