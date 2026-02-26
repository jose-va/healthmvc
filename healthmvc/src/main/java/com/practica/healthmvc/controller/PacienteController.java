package com.practica.healthmvc.controller;

import com.practica.healthmvc.dto.PacienteDTO;
import com.practica.healthmvc.service.PacienteService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
@RequestMapping("/paciente")
public class PacienteController {

    private final PacienteService pacienteService;

    public PacienteController(PacienteService pacienteService) {
        this.pacienteService = pacienteService;
    }

    @GetMapping("/perfil")
    public String verPerfil(Authentication authentication, Model model) {
        PacienteDTO perfil = pacienteService.obtenerPerfilPorUsername(authentication.getName());
        model.addAttribute("perfil", perfil);
        return "perfil";
    }

    @GetMapping("/perfil/editar")
    public String editarPerfilForm(Authentication authentication, Model model) {
        PacienteDTO perfil = pacienteService.obtenerPerfilPorUsername(authentication.getName());
        model.addAttribute("perfil", perfil);
        return "editar-perfil";
    }

    @PostMapping("/perfil/editar")
    public String editarPerfil(Authentication authentication, @Valid @ModelAttribute("perfil") PacienteDTO dto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "editar-perfil";
        }
        pacienteService.actualizarPerfil(authentication.getName(), dto);
        return "redirect:/paciente/perfil";
    }
}
