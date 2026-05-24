package com.escuelaflorece.sistema_gestion.controller;

import com.escuelaflorece.sistema_gestion.model.Nota;
import com.escuelaflorece.sistema_gestion.service.NotaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/notas")
public class NotaController {

    @Autowired
    private NotaService notaService;

    @GetMapping
    public List<Nota> listar() {
        return notaService.listarTodas();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Nota> obtenerPorId(@PathVariable Integer id) {
        return notaService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Nota> guardar(@RequestBody Nota nota) {
        return ResponseEntity.ok(notaService.guardar(nota));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Nota> actualizar(@PathVariable Integer id, @RequestBody Nota nota) {
        return notaService.buscarPorId(id).map(n -> {
            nota.setId(id);
            return ResponseEntity.ok(notaService.guardar(nota));
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        if (notaService.buscarPorId(id).isPresent()) {
            notaService.eliminar(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}