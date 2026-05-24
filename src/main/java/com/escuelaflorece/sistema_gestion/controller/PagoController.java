package com.escuelaflorece.sistema_gestion.controller;

import com.escuelaflorece.sistema_gestion.model.Pago;
import com.escuelaflorece.sistema_gestion.repository.PagoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/pagos")
public class PagoController {

    @Autowired
    private PagoRepository pagoRepository;

    @GetMapping("/listar")
    public List<Pago> listarPagos() {
        return pagoRepository.findAll();
    }

    @PostMapping("/registrar")
    public ResponseEntity<Pago> registrarPago(@RequestBody @NonNull Pago pago) {
        Pago nuevoPago = pagoRepository.save(pago);
        return ResponseEntity.ok(nuevoPago);
    }
}