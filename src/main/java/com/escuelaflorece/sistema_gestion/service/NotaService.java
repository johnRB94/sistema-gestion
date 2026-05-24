package com.escuelaflorece.sistema_gestion.service;

import com.escuelaflorece.sistema_gestion.model.Nota;
import com.escuelaflorece.sistema_gestion.repository.NotaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class NotaService {

    @Autowired
    private NotaRepository notaRepository;

    public List<Nota> listarTodas() {
        return notaRepository.findAll();
    }

    public Optional<Nota> buscarPorId(Integer id) {
        return notaRepository.findById(id);
    }

    public Nota guardar(Nota nota) {
        return notaRepository.save(nota);
    }

    public void eliminar(Integer id) {
        notaRepository.deleteById(id);
    }

    public List<Nota> buscarPorEstudiante(Long estudianteId) {
        return notaRepository.findByEstudianteId(estudianteId);
    }
}