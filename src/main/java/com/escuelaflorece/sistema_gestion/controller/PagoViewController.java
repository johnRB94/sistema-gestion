package com.escuelaflorece.sistema_gestion.controller;

import com.escuelaflorece.sistema_gestion.model.Pago;
import com.escuelaflorece.sistema_gestion.service.PagoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/pagos")
public class PagoViewController {

    @Autowired
    private PagoService pagoService;

    @GetMapping
    public String listar(Model model, Authentication auth) {
        model.addAttribute("pagos", pagoService.listarTodos());
        model.addAttribute("rolUsuario", getRol(auth));
        model.addAttribute("nombreUsuario", auth.getName());
        return "pagos_list";
    }

    @GetMapping("/nuevo")
    public String formularioNuevo(Model model, Authentication auth) {
        model.addAttribute("pago", new Pago());
        model.addAttribute("rolUsuario", getRol(auth));
        model.addAttribute("nombreUsuario", auth.getName());
        model.addAttribute("titulo", "Registrar Pago");
        return "pago_form";
    }

    @GetMapping("/editar/{id}")
    public String formularioEditar(@PathVariable Integer id, Model model, Authentication auth) {
        Pago pago = pagoService.listarTodos().stream()
                .filter(p -> p.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("ID inválido: " + id));
        model.addAttribute("pago", pago);
        model.addAttribute("rolUsuario", getRol(auth));
        model.addAttribute("nombreUsuario", auth.getName());
        model.addAttribute("titulo", "Editar Pago");
        return "pago_form";
    }

    @PostMapping("/guardar")
    public String guardar(@ModelAttribute Pago pago, RedirectAttributes ra) {
        try {
            pagoService.guardar(pago);
            ra.addFlashAttribute("successMsg", "Pago registrado correctamente.");
        } catch (Exception e) {
            ra.addFlashAttribute("errorMsg", "Error al registrar el pago: " + e.getMessage());
        }
        return "redirect:/pagos";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Integer id, RedirectAttributes ra) {
        try {
            pagoService.eliminar(id);
            ra.addFlashAttribute("successMsg", "Pago eliminado correctamente.");
        } catch (Exception e) {
            ra.addFlashAttribute("errorMsg", "No se puede eliminar el pago.");
        }
        return "redirect:/pagos";
    }

    private String getRol(Authentication auth) {
        if (auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN")))
            return "Administrador";
        if (auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_DOCENTE")))
            return "Docente";
        return "Usuario";
    }
}