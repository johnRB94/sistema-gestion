package com.escuelaflorece.sistema_gestion.controller;

import com.escuelaflorece.sistema_gestion.model.Usuario;
import com.escuelaflorece.sistema_gestion.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/usuarios")
public class UsuarioViewController {

    @Autowired
    private UsuarioService usuarioService;

    @GetMapping
    public String listar(Model model, Authentication auth) {
        model.addAttribute("usuarios", usuarioService.listarTodos());
        model.addAttribute("rolUsuario", getRol(auth));
        model.addAttribute("nombreUsuario", auth.getName());
        return "usuarios_list";
    }

    @GetMapping("/nuevo")
    public String formularioNuevo(Model model, Authentication auth) {
        model.addAttribute("usuario", new Usuario());
        model.addAttribute("rolUsuario", getRol(auth));
        model.addAttribute("nombreUsuario", auth.getName());
        model.addAttribute("titulo", "Nuevo Usuario");
        return "usuario_form";
    }

    @GetMapping("/editar/{id}")
    public String formularioEditar(@PathVariable Long id, Model model, Authentication auth) {
        Usuario usuario = usuarioService.buscarPorId(id)
                .orElseThrow(() -> new IllegalArgumentException("ID inválido: " + id));
        model.addAttribute("usuario", usuario);
        model.addAttribute("rolUsuario", getRol(auth));
        model.addAttribute("nombreUsuario", auth.getName());
        model.addAttribute("titulo", "Editar Usuario");
        return "usuario_form";
    }

    @PostMapping("/guardar")
    public String guardar(@ModelAttribute Usuario usuario) {
        usuarioService.guardarDesdeVista(usuario);
        return "redirect:/usuarios";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Long id) {
        usuarioService.eliminar(id);
        return "redirect:/usuarios";
    }

    private String getRol(Authentication auth) {
        if (auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN")))
            return "Administrador";
        if (auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_DOCENTE")))
            return "Docente";
        return "Usuario";
    }
}