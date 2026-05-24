package com.escuelaflorece.sistema_gestion.controller;

import com.escuelaflorece.sistema_gestion.model.Docente;
import com.escuelaflorece.sistema_gestion.repository.DocenteRepository;
import com.escuelaflorece.sistema_gestion.service.DocenteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/docentes")
public class DocenteController {

    @Autowired
    private DocenteRepository docenteRepository;

    @Autowired
    private DocenteService docenteService;

    @GetMapping
    public List<Docente> listar() {
        return docenteRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Docente> obtenerPorId(@PathVariable Long id) {
        return docenteRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Body: { "nombre":"Juan", "apellido":"Lopez", "email":"juan@escuelaflorece.edu.pe",
    //         "contrasena":"123456", "nivelId":2, "especialidad1":"Matematica" }
    @PostMapping("/registrar")
    public ResponseEntity<?> registrar(@RequestBody Map<String, Object> body) {
        try {
            Docente docente = new Docente();
            docente.setNombre((String) body.get("nombre"));
            docente.setApellido((String) body.get("apellido"));
            docente.setEmail((String) body.get("email"));
            docente.setEspecialidad1((String) body.get("especialidad1"));
            docente.setEspecialidad2((String) body.get("especialidad2"));
            docente.setTelefono((String) body.get("telefono"));
            if (body.get("nivelId") != null)
                docente.setNivelId(Integer.parseInt(body.get("nivelId").toString()));

            String contrasena = (String) body.get("contrasena");
            if (contrasena == null || contrasena.trim().isEmpty())
                return ResponseEntity.badRequest().body("La contraseña es obligatoria");

            Docente guardado = docenteService.guardar(docente, contrasena);
            return ResponseEntity.ok(guardado);

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error: " + e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Docente> actualizar(@PathVariable Long id, @RequestBody Docente docente) {
        return docenteRepository.findById(id).map(d -> {
            docente.setId(id);
            return ResponseEntity.ok(docenteRepository.save(docente));
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminar(@PathVariable Long id) {
        if (docenteRepository.existsById(id)) {
            docenteRepository.deleteById(id);
            return ResponseEntity.ok("Docente eliminado correctamente");
        }
        return ResponseEntity.notFound().build();
    }
}