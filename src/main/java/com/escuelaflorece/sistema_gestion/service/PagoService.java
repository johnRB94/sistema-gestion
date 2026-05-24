package com.escuelaflorece.sistema_gestion.service;

import com.escuelaflorece.sistema_gestion.model.Pago;
import com.escuelaflorece.sistema_gestion.repository.PagoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class PagoService {

    @Autowired
    private PagoRepository pagoRepository;

    public List<Pago> listarTodos() {
        return pagoRepository.findAll();
    }

    public @NonNull Pago guardar(@NonNull Pago pago) {
        return pagoRepository.save(pago);
    }

    public void eliminar(@NonNull Integer id) {
        pagoRepository.deleteById(id);
    }
}