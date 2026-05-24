package com.escuelaflorece.sistema_gestion.service;

import com.escuelaflorece.sistema_gestion.model.Docente;
import com.escuelaflorece.sistema_gestion.model.Usuario;
import com.escuelaflorece.sistema_gestion.repository.DocenteRepository;
import com.escuelaflorece.sistema_gestion.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class DocenteService {

    @Autowired
    private DocenteRepository docenteRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public List<Docente> listarTodos() {
        return docenteRepository.findAll();
    }

    @Transactional
    public Docente guardar(Docente docente, String contrasena) {
        if (!docente.getEmail().endsWith("@escuelaflorece.edu.pe")) {
            throw new IllegalArgumentException("El email debe ser @escuelaflorece.edu.pe");
        }

        // Si es nuevo docente, crear usuario automáticamente
        if (docente.getId() == null) {
            if (usuarioRepository.findByEmail(docente.getEmail()).isPresent()) {
                throw new IllegalArgumentException("Ya existe un usuario con ese email");
            }
            Usuario usuario = new Usuario();
            usuario.setNombre(docente.getNombre());
            usuario.setApellido(docente.getApellido());
            usuario.setEmail(docente.getEmail());
            usuario.setContrasena(passwordEncoder.encode(contrasena));
            usuario.setRol(2); // ROLE_DOCENTE
            usuario.setEstado("Activo");
            Usuario usuarioGuardado = usuarioRepository.save(usuario);
            docente.setUsuarioId(usuarioGuardado.getId());
        }

        return docenteRepository.save(docente);
    }

    public Docente guardar(Docente docente) {
        return docenteRepository.save(docente);
    }
}