package com.escuelaflorece.sistema_gestion.controller;

import com.escuelaflorece.sistema_gestion.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ViewController {

    @Autowired private DocenteService docenteService;
    @Autowired private EstudianteService estudianteService;
    @Autowired private AsistenciaService asistenciaService;
    @Autowired private PagoService pagoService;
    @Autowired private NotaService notaService;

    @GetMapping("/dashboard")
    public String dashboard(Model model, Authentication auth) {
        model.addAttribute("rolUsuario", getRol(auth));
        model.addAttribute("nombreUsuario", auth.getName());
        model.addAttribute("totalEstudiantes", estudianteService.listarTodos().size());
        model.addAttribute("totalDocentes", docenteService.listarTodos().size());
        model.addAttribute("totalAsistencias", asistenciaService.listarTodas().size());
        model.addAttribute("totalPagos", pagoService.listarTodos().size());
        model.addAttribute("totalNotas", notaService.listarTodas().size());
        return "dashboard";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/")
    public String raiz() {
        return "redirect:/dashboard";
    }

    private String getRol(Authentication auth) {
        if (auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN")))
            return "Administrador";
        if (auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_DOCENTE")))
            return "Docente";
        return "Usuario";
    }
}