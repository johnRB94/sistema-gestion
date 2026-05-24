package com.escuelaflorece.sistema_gestion.controller;

import com.escuelaflorece.sistema_gestion.model.Docente;
import com.escuelaflorece.sistema_gestion.repository.DocenteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/docentes")
public class DocenteController {

    @Autowired
    private DocenteRepository docenteRepository;

    // GET /api/docentes — listar todos
    @GetMapping
    public List<Docente> listar() {
        return docenteRepository.findAll();
    }

    // GET /api/docentes/{id}
    @GetMapping("/{id}")
    public ResponseEntity<Docente> obtenerPorId(@PathVariable Long id) {
        return docenteRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // POST /api/docentes
    @PostMapping
    public ResponseEntity<Docente> guardar(@RequestBody Docente docente) {
        Docente guardado = docenteRepository.save(docente);
        return ResponseEntity.ok(guardado);
    }

    // PUT /api/docentes/{id}
    @PutMapping("/{id}")
    public ResponseEntity<Docente> actualizar(@PathVariable Long id, @RequestBody Docente docente) {
        return docenteRepository.findById(id).map(d -> {
            docente.setId(id);
            return ResponseEntity.ok(docenteRepository.save(docente));
        }).orElse(ResponseEntity.notFound().build());
    }

    // DELETE /api/docentes/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminar(@PathVariable Long id) {
        if (docenteRepository.existsById(id)) {
            docenteRepository.deleteById(id);
            return ResponseEntity.ok("Docente eliminado correctamente");
        }
        return ResponseEntity.notFound().build();
    }
}