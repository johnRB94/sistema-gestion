package com.escuelaflorece.sistema_gestion.controller;

import com.escuelaflorece.sistema_gestion.model.Asistencia;
import com.escuelaflorece.sistema_gestion.repository.CursoRepository;
import com.escuelaflorece.sistema_gestion.repository.EstudianteRepository;
import com.escuelaflorece.sistema_gestion.service.AsistenciaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/asistencias")
public class AsistenciaViewController {

    @Autowired private AsistenciaService asistenciaService;
    @Autowired private EstudianteRepository estudianteRepository;
    @Autowired private CursoRepository cursoRepository;

    @GetMapping
    public String listar(Model model, Authentication auth) {
        model.addAttribute("asistencias", asistenciaService.listarTodas());
        model.addAttribute("estudiantes", estudianteRepository.findAll());
        model.addAttribute("cursos", cursoRepository.findAll());
        model.addAttribute("rolUsuario", getRol(auth));
        model.addAttribute("nombreUsuario", auth.getName());
        return "asistencias_list";
    }

    @GetMapping("/nueva")
    public String formularioNueva(Model model, Authentication auth) {
        model.addAttribute("asistencia", new Asistencia());
        model.addAttribute("estudiantes", estudianteRepository.findAll());
        model.addAttribute("cursos", cursoRepository.findAll());
        model.addAttribute("rolUsuario", getRol(auth));
        model.addAttribute("nombreUsuario", auth.getName());
        model.addAttribute("titulo", "Registrar Asistencia");
        return "asistencia_form";
    }

    @GetMapping("/editar/{id}")
    public String formularioEditar(@PathVariable Integer id, Model model, Authentication auth) {
        Asistencia asistencia = asistenciaService.buscarPorId(id)
                .orElseThrow(() -> new IllegalArgumentException("ID inválido: " + id));
        model.addAttribute("asistencia", asistencia);
        model.addAttribute("estudiantes", estudianteRepository.findAll());
        model.addAttribute("cursos", cursoRepository.findAll());
        model.addAttribute("rolUsuario", getRol(auth));
        model.addAttribute("nombreUsuario", auth.getName());
        model.addAttribute("titulo", "Editar Asistencia");
        return "asistencia_form";
    }

    @PostMapping("/guardar")
    public String guardar(@ModelAttribute Asistencia asistencia, RedirectAttributes ra) {
        try {
            asistenciaService.guardar(asistencia);
            ra.addFlashAttribute("successMsg", "Asistencia registrada correctamente.");
        } catch (Exception e) {
            ra.addFlashAttribute("errorMsg", "Error al guardar la asistencia: " + e.getMessage());
        }
        return "redirect:/asistencias";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Integer id, RedirectAttributes ra) {
        try {
            asistenciaService.eliminar(id);
            ra.addFlashAttribute("successMsg", "Asistencia eliminada correctamente.");
        } catch (Exception e) {
            ra.addFlashAttribute("errorMsg", "No se puede eliminar la asistencia.");
        }
        return "redirect:/asistencias";
    }

    private String getRol(Authentication auth) {
        if (auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN")))
            return "Administrador";
        if (auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_DOCENTE")))
            return "Docente";
        return "Usuario";
    }
}