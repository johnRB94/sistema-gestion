package com.escuelaflorece.sistema_gestion.service;

import com.escuelaflorece.sistema_gestion.model.Curso;
import com.escuelaflorece.sistema_gestion.repository.CursoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class CursoService {

    @Autowired
    private CursoRepository cursoRepository;

    public List<Curso> listarTodos() {
        return cursoRepository.findAll();
    }

    public Curso guardar(Curso curso) {
        if (curso == null) {
            throw new IllegalArgumentException("El curso no puede ser nulo");
        }
        return cursoRepository.save(curso);
    }
}