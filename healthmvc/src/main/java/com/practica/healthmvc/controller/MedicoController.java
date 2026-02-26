package com.practica.healthmvc.controller;

import com.practica.healthmvc.dto.MedicoDTO;
import com.practica.healthmvc.service.MedicoService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
@RequestMapping("/medico")
public class MedicoController {

    private final MedicoService medicoService;

    public MedicoController(MedicoService medicoService) {
        this.medicoService = medicoService;
    }

    @GetMapping("/perfil")
    public String verPerfil(Authentication authentication, Model model) {
        MedicoDTO perfil = medicoService.obtenerPerfilPorUsername(authentication.getName());
        model.addAttribute("perfil", perfil);
        return "perfil-medico";
    }

    @GetMapping("/perfil/editar")
    public String editarPerfilForm(Authentication authentication, Model model) {
        MedicoDTO perfil = medicoService.obtenerPerfilPorUsername(authentication.getName());
        model.addAttribute("perfil", perfil);
        return "editar-perfil-medico";
    }

    @PostMapping("/perfil/editar")
    public String editarPerfil(Authentication authentication, @Valid @ModelAttribute("perfil") MedicoDTO dto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "editar-perfil-medico";
        }
        medicoService.actualizarPerfil(authentication.getName(), dto);
        return "redirect:/medico/perfil";
    }
}
