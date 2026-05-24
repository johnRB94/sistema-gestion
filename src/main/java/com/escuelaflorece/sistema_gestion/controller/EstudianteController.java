package com.escuelaflorece.sistema_gestion.controller;

import com.escuelaflorece.sistema_gestion.model.Estudiante;
import com.escuelaflorece.sistema_gestion.service.EstudianteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/estudiantes")
public class EstudianteController {

    @Autowired
    private EstudianteService estudianteService;

    // GET /api/estudiantes
    @GetMapping
    public List<Estudiante> listar() {
        return estudianteService.listarTodos();
    }

    // GET /api/estudiantes/{id}
    @GetMapping("/{id}")
    public ResponseEntity<Estudiante> obtenerPorId(@PathVariable Long id) {
        return estudianteService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // POST /api/estudiantes
    @PostMapping
    public ResponseEntity<Estudiante> guardar(@RequestBody Estudiante estudiante) {
        if (estudiante.getEstado() == null) estudiante.setEstado("Activo");
        return ResponseEntity.ok(estudianteService.guardar(estudiante));
    }

    // PUT /api/estudiantes/{id}
    @PutMapping("/{id}")
    public ResponseEntity<Estudiante> actualizar(@PathVariable Long id, @RequestBody Estudiante estudiante) {
        return estudianteService.buscarPorId(id).map(est -> {
            estudiante.setId(id);
            return ResponseEntity.ok(estudianteService.guardar(estudiante));
        }).orElse(ResponseEntity.notFound().build());
    }

    // DELETE /api/estudiantes/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminar(@PathVariable Long id) {
        if (estudianteService.buscarPorId(id).isPresent()) {
            estudianteService.eliminar(id);
            return ResponseEntity.ok("Estudiante eliminado correctamente");
        }
        return ResponseEntity.notFound().build();
    }
}