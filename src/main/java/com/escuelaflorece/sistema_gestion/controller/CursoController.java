package com.escuelaflorece.sistema_gestion.controller;

import com.escuelaflorece.sistema_gestion.model.Curso;
import com.escuelaflorece.sistema_gestion.repository.CursoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/cursos")
public class CursoController {
    @Autowired
    private CursoRepository cursoRepository;

    @GetMapping("/listar")
    public List<Curso> listar() {
        return cursoRepository.findAll();
    }
}