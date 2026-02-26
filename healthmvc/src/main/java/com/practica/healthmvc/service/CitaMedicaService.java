package com.practica.healthmvc.service;

import com.practica.healthmvc.dto.CitaMedicaDTO;
import com.practica.healthmvc.model.CitaMedica;
import com.practica.healthmvc.model.Medico;
import com.practica.healthmvc.model.Paciente;
import com.practica.healthmvc.repository.CitaMedicaRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CitaMedicaService {

    private final CitaMedicaRepository citaMedicaRepository;
    private final PacienteService pacienteService;
    private final MedicoService medicoService;

    public CitaMedicaService(CitaMedicaRepository citaMedicaRepository, PacienteService pacienteService, MedicoService medicoService) {
        this.citaMedicaRepository = citaMedicaRepository;
        this.pacienteService = pacienteService;
        this.medicoService = medicoService;
    }

    public List<CitaMedicaDTO> obtenerCitasPorPaciente(String username) {
        Paciente paciente = pacienteService.obtenerEntidadPorUsername(username);
        return citaMedicaRepository.findByPacienteIdOrderByFechaAscHoraAsc(paciente.getId())
                .stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    public List<CitaMedicaDTO> obtenerCitasPorMedico(String username) {
        Medico medico = medicoService.obtenerEntidadPorUsername(username);
        return citaMedicaRepository.findByMedicoIdOrderByFechaAscHoraAsc(medico.getId())
                .stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    public void solicitarCita(String username, CitaMedicaDTO dto) {
        Paciente paciente = pacienteService.obtenerEntidadPorUsername(username);
        
        CitaMedica cita = new CitaMedica();
        cita.setFecha(dto.getFecha());
        cita.setHora(dto.getHora());
        cita.setEspecialidad(dto.getEspecialidad());
        cita.setDescripcion(dto.getDescripcion());
        cita.setEstado(CitaMedica.EstadoCita.PROGRAMADA);
        cita.setPaciente(paciente);
        
        citaMedicaRepository.save(cita);
    }

    public CitaMedicaDTO obtenerCitaPorId(Long id) {
        CitaMedica cita = citaMedicaRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Cita no encontrada"));
        return convertirADTO(cita);
    }

    public void actualizarCita(Long id, CitaMedicaDTO dto, String username) {
        CitaMedica cita = citaMedicaRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Cita no encontrada"));

        if (!cita.getPaciente().getUsuario().getUsername().equals(username)) {
            throw new IllegalArgumentException("No tiene permisos para modificar esta cita");
        }
        
        cita.setFecha(dto.getFecha());
        cita.setHora(dto.getHora());
        cita.setEspecialidad(dto.getEspecialidad());
        cita.setDescripcion(dto.getDescripcion());
        
        citaMedicaRepository.save(cita);
    }

    public void cancelarCita(Long id, String username) {
        CitaMedica cita = citaMedicaRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Cita no encontrada"));
        
        if (!cita.getPaciente().getUsuario().getUsername().equals(username)) {
            throw new IllegalArgumentException("No tiene permisos para cancelar esta cita");
        }
        
        cita.setEstado(CitaMedica.EstadoCita.CANCELADA);
        citaMedicaRepository.save(cita);
    }

    private CitaMedicaDTO convertirADTO(CitaMedica cita) {
        CitaMedicaDTO dto = new CitaMedicaDTO();
        dto.setId(cita.getId());
        dto.setFecha(cita.getFecha());
        dto.setHora(cita.getHora());
        dto.setEspecialidad(cita.getEspecialidad());
        dto.setDescripcion(cita.getDescripcion());
        dto.setEstado(cita.getEstado().name());
        dto.setPacienteId(cita.getPaciente().getId());
        dto.setPacienteNombre(cita.getPaciente().getNombre() + " " + cita.getPaciente().getApellidos());
        if (cita.getMedico() != null) {
            dto.setMedicoId(cita.getMedico().getId());
            dto.setMedicoNombre(cita.getMedico().getNombre() + " " + cita.getMedico().getApellidos());
        }
        return dto;
    }
}
