package com.escuelaflorece.sistema_gestion.controller;

import com.escuelaflorece.sistema_gestion.model.Asistencia;
import com.escuelaflorece.sistema_gestion.service.AsistenciaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Objects;

@RestController
@RequestMapping("/api/asistencias")
public class AsistenciaController {

    @Autowired
    private AsistenciaService asistenciaService;

    // 1. LISTAR TODAS LAS ASISTENCIAS
    @GetMapping
    public List<Asistencia> listar() {
        return asistenciaService.listarTodas();
    }

    // 2. BUSCAR POR ID
    @GetMapping("/{id}")
    public ResponseEntity<Asistencia> obtenerPorId(@PathVariable int id) {
        return asistenciaService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // 3. REGISTRAR UNA NUEVA ASISTENCIA
    @PostMapping
    public ResponseEntity<Asistencia> guardar(@RequestBody Asistencia asistencia) {

        if (asistencia == null) {
            return ResponseEntity.badRequest().build();
        }

        Asistencia nueva = asistenciaService.guardar(asistencia);
        return ResponseEntity.ok(nueva);
    }

    // 4. MODIFICAR SOLO EL ESTADO
    @PatchMapping("/{id}")
    public ResponseEntity<Asistencia> modificarEstado(
            @PathVariable int id,
            @RequestBody Map<String, Object> updates) {

        return asistenciaService.buscarPorId(id)
                .map(asistencia -> {

                    if (updates.containsKey("estado")) {
                        asistencia.setEstado((String) updates.get("estado"));
                    }

                    Asistencia guardado =
                            Objects.requireNonNull(asistenciaService.guardar(asistencia));

                    return ResponseEntity.ok(guardado);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    // 5. ELIMINAR UNA ASISTENCIA
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable int id) {

        if (asistenciaService.buscarPorId(id).isPresent()) {
            asistenciaService.eliminar(id);
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.notFound().build();
    }
}