package com.escuelaflorece.sistema_gestion.controller;

import com.escuelaflorece.sistema_gestion.model.Nota;
import com.escuelaflorece.sistema_gestion.repository.CursoRepository;
import com.escuelaflorece.sistema_gestion.repository.EstudianteRepository;
import com.escuelaflorece.sistema_gestion.service.NotaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/notas")
public class NotaViewController {

    @Autowired private NotaService notaService;
    @Autowired private EstudianteRepository estudianteRepository;
    @Autowired private CursoRepository cursoRepository;

    @GetMapping
    public String listar(Model model, Authentication auth) {
        model.addAttribute("notas", notaService.listarTodas());
        model.addAttribute("rolUsuario", getRol(auth));
        model.addAttribute("nombreUsuario", auth.getName());
        return "notas_list";
    }

    @GetMapping("/nueva")
    public String formularioNueva(Model model, Authentication auth) {
        model.addAttribute("nota", new Nota());
        model.addAttribute("estudiantes", estudianteRepository.findAll());
        model.addAttribute("cursos", cursoRepository.findAll());
        model.addAttribute("rolUsuario", getRol(auth));
        model.addAttribute("nombreUsuario", auth.getName());
        model.addAttribute("titulo", "Nueva Nota");
        return "nota_form";
    }

    @GetMapping("/editar/{id}")
    public String formularioEditar(@PathVariable Integer id, Model model, Authentication auth) {
        Nota nota = notaService.buscarPorId(id)
                .orElseThrow(() -> new IllegalArgumentException("ID inválido: " + id));
        model.addAttribute("nota", nota);
        model.addAttribute("estudiantes", estudianteRepository.findAll());
        model.addAttribute("cursos", cursoRepository.findAll());
        model.addAttribute("rolUsuario", getRol(auth));
        model.addAttribute("nombreUsuario", auth.getName());
        model.addAttribute("titulo", "Editar Nota");
        return "nota_form";
    }

    @PostMapping("/guardar")
    public String guardar(@ModelAttribute Nota nota, RedirectAttributes ra) {
        try {
            notaService.guardar(nota);
            ra.addFlashAttribute("successMsg", "Nota guardada correctamente.");
        } catch (Exception e) {
            ra.addFlashAttribute("errorMsg", "Error al guardar la nota: " + e.getMessage());
        }
        return "redirect:/notas";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Integer id, RedirectAttributes ra) {
        try {
            notaService.eliminar(id);
            ra.addFlashAttribute("successMsg", "Nota eliminada correctamente.");
        } catch (Exception e) {
            ra.addFlashAttribute("errorMsg", "No se puede eliminar la nota.");
        }
        return "redirect:/notas";
    }

    private String getRol(Authentication auth) {
        if (auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN")))
            return "Administrador";
        if (auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_DOCENTE")))
            return "Docente";
        return "Usuario";
    }
}