package com.practica.healthmvc.controller;

import com.practica.healthmvc.dto.UsuarioDTO;
import com.practica.healthmvc.model.Usuario;
import com.practica.healthmvc.security.JwtUtil;
import com.practica.healthmvc.service.MedicoService;
import com.practica.healthmvc.service.UsuarioService;
import com.practica.healthmvc.service.PacienteService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@Controller
public class AuthController {

    private final UsuarioService usuarioService;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final PacienteService pacienteService;
    private final MedicoService medicoService;

    public AuthController(UsuarioService usuarioService,
                          AuthenticationManager authenticationManager,
                          JwtUtil jwtUtil,
                          PacienteService pacienteService,
                          MedicoService medicoService) {
        this.usuarioService = usuarioService;
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.pacienteService = pacienteService;
        this.medicoService = medicoService;
    }

    @GetMapping("/login")
    public String loginForm() {
        return "login";
    }

    @PostMapping("/login")
    public String login(@RequestParam String username,
                        @RequestParam String password,
                        HttpServletResponse response) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(username, password)
        );

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        String jwt = jwtUtil.generateToken(userDetails);

        Cookie cookie = new Cookie("JWT", jwt);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        cookie.setMaxAge(60 * 60);

        response.addCookie(cookie);

        return "redirect:/citas";
    }


    @GetMapping("/register")
    public String registerForm(Model model) {
        model.addAttribute("usuarioDTO", new UsuarioDTO());
        return "register";
    }

    @PostMapping("/register")
    public String register(@Valid @ModelAttribute("usuarioDTO") UsuarioDTO usuarioDTO) {

        Usuario usuario = usuarioService.crearUsuario(
                usuarioDTO.getUsername(),
                usuarioDTO.getPassword(),
                usuarioDTO.getRol()
        );

        if ("PACIENTE".equalsIgnoreCase(usuarioDTO.getRol()) || "ROLE_PACIENTE".equalsIgnoreCase(usuarioDTO.getRol())) {
            pacienteService.crearPerfilVacio(usuario);
        } else if ("MEDICO".equalsIgnoreCase(usuarioDTO.getRol()) || "ROLE_MEDICO".equalsIgnoreCase(usuarioDTO.getRol())) {
            medicoService.crearPerfilVacio(usuario);
        }

        return "redirect:/login";
    }

    @GetMapping("/logout")
    public String logout(HttpServletResponse response) {

        Cookie cookie = new Cookie("JWT", null);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        cookie.setMaxAge(0);

        response.addCookie(cookie);

        return "redirect:/login";
    }
}
