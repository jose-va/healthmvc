package com.practica.healthmvc.service;

import com.practica.healthmvc.dto.PacienteDTO;
import com.practica.healthmvc.model.Paciente;
import com.practica.healthmvc.model.Usuario;
import com.practica.healthmvc.repository.PacienteRepository;
import com.practica.healthmvc.repository.UsuarioRepository;
import org.springframework.stereotype.Service;

@Service
public class PacienteService {

    private final PacienteRepository pacienteRepository;
    private final UsuarioRepository usuarioRepository;

    public PacienteService(PacienteRepository pacienteRepository, UsuarioRepository usuarioRepository) {
        this.pacienteRepository = pacienteRepository;
        this.usuarioRepository = usuarioRepository;
    }

    public void crearPerfilVacio(Usuario usuario) {
        Paciente paciente = new Paciente();
        paciente.setUsuario(usuario);
        paciente.setNombre("No definido");
        paciente.setApellidos("No definido");
        pacienteRepository.save(paciente);
    }

    public PacienteDTO obtenerPerfilPorUsername(String username) {
        Paciente paciente = pacienteRepository.findByUsuarioUsername(username)
                .orElse(null);
        if (paciente == null) return null;
        return convertirADTO(paciente);
    }

    public void actualizarPerfil(String username, PacienteDTO dto) {
        Paciente paciente = pacienteRepository.findByUsuarioUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("Paciente no encontrado"));
        
        paciente.setNombre(dto.getNombre());
        paciente.setApellidos(dto.getApellidos());
        paciente.setFechaNacimiento(dto.getFechaNacimiento());
        paciente.setTelefono(dto.getTelefono());
        
        pacienteRepository.save(paciente);
    }

    public Paciente obtenerEntidadPorUsername(String username) {
        return pacienteRepository.findByUsuarioUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("Paciente no encontrado"));
    }

    private PacienteDTO convertirADTO(Paciente paciente) {
        PacienteDTO dto = new PacienteDTO();
        dto.setId(paciente.getId());
        dto.setNombre(paciente.getNombre());
        dto.setApellidos(paciente.getApellidos());
        dto.setFechaNacimiento(paciente.getFechaNacimiento());
        dto.setTelefono(paciente.getTelefono());
        return dto;
    }
}
