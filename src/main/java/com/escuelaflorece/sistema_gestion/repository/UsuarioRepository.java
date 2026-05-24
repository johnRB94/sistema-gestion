package com.escuelaflorece.sistema_gestion.repository;

import com.escuelaflorece.sistema_gestion.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    
    // Cambiamos a findByEmail
    Optional<Usuario> findByEmail(String email);
}