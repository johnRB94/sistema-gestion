package com.escuelaflorece.sistema_gestion.controller;

import com.escuelaflorece.sistema_gestion.model.Matricula;
import com.escuelaflorece.sistema_gestion.model.Estudiante;
import com.escuelaflorece.sistema_gestion.model.Grado;
import com.escuelaflorece.sistema_gestion.repository.EstudianteRepository;
import com.escuelaflorece.sistema_gestion.repository.GradoRepository;
import com.escuelaflorece.sistema_gestion.service.MatriculaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/matriculas")
public class MatriculaViewController {

    @Autowired private MatriculaService matriculaService;
    @Autowired private EstudianteRepository estudianteRepository;
    @Autowired private GradoRepository gradoRepository;

    @GetMapping
    public String listar(Model model, Authentication auth) {
        model.addAttribute("matriculas", matriculaService.listarTodas());

        // ✅ Mapas para mostrar nombres en lugar de IDs
        Map<Long, String> estudiantesMap = new HashMap<>();
        for (Estudiante e : estudianteRepository.findAll()) {
            estudiantesMap.put(e.getId(), e.getNombre() + " " + e.getApellido());
        }

        Map<Integer, String> gradosMap = new HashMap<>();
        for (Grado g : gradoRepository.findAll()) {
            gradosMap.put(g.getId(), g.getNombre());
        }

        model.addAttribute("estudiantesMap", estudiantesMap);
        model.addAttribute("gradosMap", gradosMap);
        model.addAttribute("rolUsuario", getRol(auth));
        model.addAttribute("nombreUsuario", auth.getName());
        return "matriculas_list";
    }

    @GetMapping("/nueva")
    public String formularioNueva(Model model, Authentication auth) {
        Matricula m = new Matricula();
        m.setFecha(LocalDate.now());
        m.setAnioAcademico(LocalDate.now().getYear());
        model.addAttribute("matricula", m);
        model.addAttribute("estudiantes", estudianteRepository.findAll());
        model.addAttribute("grados", gradoRepository.findAll());
        model.addAttribute("rolUsuario", getRol(auth));
        model.addAttribute("nombreUsuario", auth.getName());
        model.addAttribute("titulo", "Nueva Matrícula");
        return "matricula_form";
    }

    @GetMapping("/editar/{id}")
    public String formularioEditar(@PathVariable Integer id, Model model, Authentication auth) {
        Matricula matricula = matriculaService.buscarPorId(id)
                .orElseThrow(() -> new IllegalArgumentException("ID inválido: " + id));
        model.addAttribute("matricula", matricula);
        model.addAttribute("estudiantes", estudianteRepository.findAll());
        model.addAttribute("grados", gradoRepository.findAll());
        model.addAttribute("rolUsuario", getRol(auth));
        model.addAttribute("nombreUsuario", auth.getName());
        model.addAttribute("titulo", "Editar Matrícula");
        return "matricula_form";
    }

    @PostMapping("/guardar")
    public String guardar(@ModelAttribute Matricula matricula, RedirectAttributes ra) {
        try {
            matriculaService.guardar(matricula);
            ra.addFlashAttribute("successMsg", "Matrícula guardada correctamente.");
        } catch (Exception e) {
            ra.addFlashAttribute("errorMsg", "Error al guardar la matrícula: " + e.getMessage());
        }
        return "redirect:/matriculas";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Integer id, RedirectAttributes ra) {
        try {
            matriculaService.eliminar(id);
            ra.addFlashAttribute("successMsg", "Matrícula eliminada correctamente.");
        } catch (Exception e) {
            ra.addFlashAttribute("errorMsg", "No se puede eliminar: la matrícula tiene pagos o notas asociadas.");
        }
        return "redirect:/matriculas";
    }

    private String getRol(Authentication auth) {
        if (auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN")))
            return "Administrador";
        if (auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_DOCENTE")))
            return "Docente";
        return "Usuario";
    }
}