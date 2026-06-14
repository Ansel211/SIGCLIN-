package com.sigclin.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sigclin.backend.model.Paciente;

public interface PacienteRepository extends JpaRepository<Paciente, Long> {
    
}