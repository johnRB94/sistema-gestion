package com.escuelaflorece.sistema_gestion.service;

import com.escuelaflorece.sistema_gestion.model.Asistencia;
import com.escuelaflorece.sistema_gestion.repository.AsistenciaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AsistenciaService {

    @Autowired
    private AsistenciaRepository asistenciaRepository;

    public List<Asistencia> listarTodas() {
        return asistenciaRepository.findAll();
    }

    public Optional<Asistencia> buscarPorId(@NonNull Integer id) {
        return asistenciaRepository.findById(id);
    }

    public @NonNull Asistencia guardar(@NonNull Asistencia asistencia) {
        return asistenciaRepository.save(asistencia);
    }

    public void eliminar(@NonNull Integer id) {
        asistenciaRepository.deleteById(id);
    }
}