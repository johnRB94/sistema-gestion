package com.escuelaflorece.sistema_gestion.controller;

import com.escuelaflorece.sistema_gestion.model.Estudiante;
import com.escuelaflorece.sistema_gestion.repository.EstudianteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
    public String guardar(
            @RequestParam String nombre,
            @RequestParam String apellido,
            @RequestParam String dni,
            @RequestParam(required = false) String fechaNacimiento,
            @RequestParam(required = false) String telefonoContacto,
            @RequestParam(required = false, defaultValue = "Activo") String estado,
            @RequestParam Integer nivelId,
            @RequestParam Integer gradoId,
            @RequestParam(required = false) Long id,
            RedirectAttributes ra) {
        try {
            Estudiante estudiante = new Estudiante();
            if (id != null) estudiante.setId(id);
            estudiante.setNombre(nombre);
            estudiante.setApellido(apellido);
            estudiante.setDni(dni);
            estudiante.setNivelId(nivelId);
            estudiante.setGradoId(gradoId);
            estudiante.setEstado(estado != null ? estado : "Activo");
            estudiante.setTelefonoContacto(telefonoContacto);
            if (fechaNacimiento != null && !fechaNacimiento.isEmpty())
                estudiante.setFechaNacimiento(java.time.LocalDate.parse(fechaNacimiento));
            estudianteRepository.save(estudiante);
            ra.addFlashAttribute("successMsg", "Estudiante guardado correctamente.");
        } catch (Exception e) {
            ra.addFlashAttribute("errorMsg", "Error al guardar: " + e.getMessage());
        }
        return "redirect:/estudiantes";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Long id, RedirectAttributes ra) {
        try {
            estudianteRepository.deleteById(id);
            ra.addFlashAttribute("successMsg", "Estudiante eliminado correctamente.");
        } catch (Exception e) {
            ra.addFlashAttribute("errorMsg", "No se puede eliminar: tiene registros asociados.");
        }
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