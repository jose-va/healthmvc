package com.practica.healthmvc.controller;

import com.practica.healthmvc.dto.CitaMedicaDTO;
import com.practica.healthmvc.service.CitaMedicaService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/citas")
public class CitaMedicaController {

    private final CitaMedicaService citaMedicaService;

    public CitaMedicaController(CitaMedicaService citaMedicaService) {
        this.citaMedicaService = citaMedicaService;
    }

    @GetMapping
    public String listarCitas(Authentication authentication, @RequestParam(required = false) String estado, Model model) {
        List<CitaMedicaDTO> citas;
        boolean isMedico = authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_MEDICO"));

        if (isMedico) {
            citas = citaMedicaService.obtenerCitasPorMedico(authentication.getName());
        } else {
            citas = citaMedicaService.obtenerCitasPorPaciente(authentication.getName());
        }
        if (estado != null && !estado.isEmpty()) {
            citas.removeIf(cita -> !cita.getEstado().equalsIgnoreCase(estado));
        }
        model.addAttribute("citas", citas);
        return "citas";
    }

    @GetMapping("/nueva")
    public String nuevaCitaForm(Model model) {
        model.addAttribute("citaDTO", new CitaMedicaDTO());
        return "nueva-cita";
    }

    @PostMapping("/nueva")
    public String solicitarCita(Authentication authentication, @Valid @ModelAttribute("citaDTO") CitaMedicaDTO dto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "nueva-cita";
        }
        citaMedicaService.solicitarCita(authentication.getName(), dto);
        return "redirect:/citas";
    }

    @GetMapping("/{id}")
    public String verCita(@PathVariable Long id, Model model) {
        model.addAttribute("cita", citaMedicaService.obtenerCitaPorId(id));
        return "detalle-cita";
    }

    @GetMapping("/{id}/editar")
    public String editarCitaForm(@PathVariable Long id, Model model) {
        model.addAttribute("citaDTO", citaMedicaService.obtenerCitaPorId(id));
        return "editar-cita";
    }

    @PostMapping("/{id}/editar")
    public String editarCita(Authentication authentication, @PathVariable Long id, @Valid @ModelAttribute("citaDTO") CitaMedicaDTO dto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "editar-cita";
        }
        citaMedicaService.actualizarCita(id, dto, authentication.getName());
        return "redirect:/citas/" + id;
    }

    @PostMapping("/{id}/cancelar")
    public String cancelarCita(@PathVariable Long id, Authentication authentication) {
        citaMedicaService.cancelarCita(id, authentication.getName());
        return "redirect:/citas/" + id;
    }
}
