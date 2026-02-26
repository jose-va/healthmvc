package com.practica.healthmvc.repository;

import com.practica.healthmvc.model.CitaMedica;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CitaMedicaRepository extends JpaRepository<CitaMedica, Long> {
    List<CitaMedica> findByPacienteId(Long pacienteId);
    List<CitaMedica> findByPacienteIdOrderByFechaAscHoraAsc(Long pacienteId);
    List<CitaMedica> findByMedicoIdOrderByFechaAscHoraAsc(Long medicoId);
}
