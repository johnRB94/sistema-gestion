package com.escuelaflorece.sistema_gestion.service;

import com.escuelaflorece.sistema_gestion.model.Estudiante;
import com.escuelaflorece.sistema_gestion.repository.EstudianteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class EstudianteService {

    @Autowired
    private EstudianteRepository repository;

    public List<Estudiante> listarTodos() {
        return repository.findAll();
    }

    public Optional<Estudiante> buscarPorId(Long id) {
        if (id == null) {
            return Optional.empty();
        }
        return repository.findById(id);
    }

    public Estudiante guardar(Estudiante estudiante) {
        if (estudiante == null) {
            throw new IllegalArgumentException("El estudiante no puede ser nulo");
        }
        // Aquí podrías agregar validaciones: ej. validar que el DNI no exista
        return repository.save(estudiante);
    }

    public void eliminar(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("El id no puede ser nulo");
        }
        repository.deleteById(id);
    }
}