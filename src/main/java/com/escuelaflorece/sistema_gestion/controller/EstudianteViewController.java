package com.escuelaflorece.sistema_gestion.controller;

import com.escuelaflorece.sistema_gestion.model.Estudiante;
import com.escuelaflorece.sistema_gestion.repository.EstudianteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/estudiantes")
public class EstudianteViewController {

    @Autowired
    private EstudianteRepository estudianteRepository;

    @GetMapping
    public String listar(Model model, Authentication auth) {
        model.addAttribute("estudiantes", estudianteRepository.findAll());
        model.addAttribute("rolUsuario", getRol(auth));
        model.addAttribute("nombreUsuario", auth.getName());
        return "estudiantes_list";
    }

    @GetMapping("/nuevo")
    public String formularioNuevo(Model model, Authentication auth) {
        model.addAttribute("estudiante", new Estudiante());
        model.addAttribute("rolUsuario", getRol(auth));
        model.addAttribute("nombreUsuario", auth.getName());
        model.addAttribute("titulo", "Nuevo Estudiante");
        return "estudiante_form";
    }

    @GetMapping("/editar/{id}")
    public String formularioEditar(@PathVariable Long id, Model model, Authentication auth) {
        Estudiante estudiante = estudianteRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("ID invalido: " + id));
        model.addAttribute("estudiante", estudiante);
        model.addAttribute("rolUsuario", getRol(auth));
        model.addAttribute("nombreUsuario", auth.getName());
        model.addAttribute("titulo", "Editar Estudiante");
        return "estudiante_form";
    }

    @PostMapping("/guardar")
    public String guardar(@ModelAttribute Estudiante estudiante) {
        if (estudiante.getEstado() == null || estudiante.getEstado().isEmpty()) {
            estudiante.setEstado("Activo");
        }
        estudianteRepository.save(estudiante);
        return "redirect:/estudiantes";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Long id) {
        estudianteRepository.deleteById(id);
        return "redirect:/estudiantes";
    }

    private String getRol(Authentication auth) {
        if (auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN")))
            return "Administrador";
        if (auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_DOCENTE")))
            return "Docente";
        return "Usuario";
    }
}