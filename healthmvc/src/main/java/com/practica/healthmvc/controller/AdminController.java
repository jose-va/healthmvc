package com.practica.healthmvc.controller;

import com.practica.healthmvc.repository.UsuarioRepository;
import com.practica.healthmvc.service.UsuarioService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    private final UsuarioRepository userRepo;
    private final UsuarioService usuarioService;

    public AdminController(UsuarioRepository userRepo,
                           UsuarioService usuarioService) {
        this.userRepo = userRepo;
        this.usuarioService = usuarioService;
    }

    @GetMapping("/users")
    public String allUsers(Model model) {
        model.addAttribute("users", userRepo.findAll());
        return "admin-users";
    }

    @PostMapping("/users/delete/{id}")
    public String deleteUser(@PathVariable Long id) {
        usuarioService.eliminarUsuario(id);
        return "redirect:/admin/users";
    }
}