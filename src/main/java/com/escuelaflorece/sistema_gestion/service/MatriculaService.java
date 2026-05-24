package com.escuelaflorece.sistema_gestion.service;

import com.escuelaflorece.sistema_gestion.model.Matricula;
import com.escuelaflorece.sistema_gestion.repository.MatriculaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class MatriculaService {

    @Autowired
    private MatriculaRepository matriculaRepository;

    public List<Matricula> listarTodas() {
        return matriculaRepository.findAll();
    }

    public Optional<Matricula> buscarPorId(Integer id) {
        return matriculaRepository.findById(id);
    }

    public Matricula guardar(Matricula matricula) {
        return matriculaRepository.save(matricula);
    }

    public void eliminar(Integer id) {
        matriculaRepository.deleteById(id);
    }
}