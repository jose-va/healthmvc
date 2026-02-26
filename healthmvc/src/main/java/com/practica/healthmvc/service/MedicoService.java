package com.practica.healthmvc.service;

import com.practica.healthmvc.dto.MedicoDTO;
import com.practica.healthmvc.model.Medico;
import com.practica.healthmvc.model.Usuario;
import com.practica.healthmvc.repository.MedicoRepository;
import org.springframework.stereotype.Service;

@Service
public class MedicoService {

    private final MedicoRepository medicoRepository;

    public MedicoService(MedicoRepository medicoRepository) {
        this.medicoRepository = medicoRepository;
    }

    public void crearPerfilVacio(Usuario usuario) {
        Medico medico = new Medico();
        medico.setUsuario(usuario);
        medico.setNombre("No definido");
        medico.setApellidos("No definido");
        medico.setEspecialidad("No definida");
        medicoRepository.save(medico);
    }

    public MedicoDTO obtenerPerfilPorUsername(String username) {
        Medico medico = medicoRepository.findByUsuarioUsername(username).orElse(null);
        if (medico == null) return null;
        return convertirADTO(medico);
    }

    public void actualizarPerfil(String username, MedicoDTO dto) {
        Medico medico = medicoRepository.findByUsuarioUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("Medico no encontrado"));

        medico.setNombre(dto.getNombre());
        medico.setApellidos(dto.getApellidos());
        medico.setEspecialidad(dto.getEspecialidad());

        medicoRepository.save(medico);
    }

    public Medico obtenerEntidadPorUsername(String username) {
        return medicoRepository.findByUsuarioUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("Medico no encontrado"));
    }

    private MedicoDTO convertirADTO(Medico medico) {
        MedicoDTO dto = new MedicoDTO();
        dto.setId(medico.getId());
        dto.setNombre(medico.getNombre());
        dto.setApellidos(medico.getApellidos());
        dto.setEspecialidad(medico.getEspecialidad());
        return dto;
    }
}
