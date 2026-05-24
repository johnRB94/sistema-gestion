package com.escuelaflorece.sistema_gestion.controller;

import com.escuelaflorece.sistema_gestion.model.Docente;
import com.escuelaflorece.sistema_gestion.repository.DocenteRepository;
import com.escuelaflorece.sistema_gestion.service.DocenteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/docentes")
public class DocenteViewController {

    @Autowired
    private DocenteRepository docenteRepository;

    @Autowired
    private DocenteService docenteService;

    @GetMapping
    public String listar(Model model, Authentication auth) {
        model.addAttribute("listaDocentes", docenteRepository.findAll());
        model.addAttribute("rolUsuario", getRol(auth));
        model.addAttribute("nombreUsuario", auth.getName());
        return "docentes_list";
    }

    @GetMapping("/nuevo")
    public String formularioNuevo(Model model, Authentication auth) {
        model.addAttribute("docente", new Docente());
        model.addAttribute("rolUsuario", getRol(auth));
        model.addAttribute("nombreUsuario", auth.getName());
        model.addAttribute("titulo", "Nuevo Docente");
        model.addAttribute("esNuevo", true);
        return "docente_form";
    }

    @GetMapping("/editar/{id}")
    public String formularioEditar(@PathVariable Long id, Model model, Authentication auth) {
        Docente docente = docenteRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("ID inválido: " + id));
        model.addAttribute("docente", docente);
        model.addAttribute("rolUsuario", getRol(auth));
        model.addAttribute("nombreUsuario", auth.getName());
        model.addAttribute("titulo", "Editar Docente");
        model.addAttribute("esNuevo", false);
        return "docente_form";
    }

    @PostMapping("/guardar")
    public String guardar(@ModelAttribute Docente docente,
                          @RequestParam(required = false) String contrasena,
                          RedirectAttributes ra) {
        try {
            if (docente.getId() == null) {
                // Nuevo docente: requiere contraseña
                if (contrasena == null || contrasena.trim().isEmpty()) {
                    ra.addFlashAttribute("errorMsg", "La contraseña es obligatoria para nuevos docentes.");
                    return "redirect:/docentes/nuevo";
                }
                docenteService.guardar(docente, contrasena);
            } else {
                // Editar: solo actualiza datos del docente
                docenteService.guardar(docente);
            }
            ra.addFlashAttribute("successMsg", "Docente guardado correctamente.");
        } catch (IllegalArgumentException e) {
            ra.addFlashAttribute("errorMsg", e.getMessage());
            return "redirect:/docentes/nuevo";
        } catch (Exception e) {
            ra.addFlashAttribute("errorMsg", "Error al guardar: " + e.getMessage());
        }
        return "redirect:/docentes";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Long id, RedirectAttributes ra) {
        try {
            docenteRepository.deleteById(id);
            ra.addFlashAttribute("successMsg", "Docente eliminado correctamente.");
        } catch (Exception e) {
            ra.addFlashAttribute("errorMsg", "No se puede eliminar: el docente tiene registros asociados.");
        }
        return "redirect:/docentes";
    }

    private String getRol(Authentication auth) {
        if (auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN")))
            return "Administrador";
        if (auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_DOCENTE")))
            return "Docente";
        return "Usuario";
    }
}