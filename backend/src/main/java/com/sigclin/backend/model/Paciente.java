package com.sigclin.backend.model;

import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "paciente")
public class Paciente {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idPaciente;
    
    private String nombres;
    private String apellidoPaterno;
    private String apellidoMaterno;
    private String numDocumento;
    private String telefono;
    private String direccion;
    private LocalDate fechaNacimiento;
    
    // ❌ BORRA ESTAS LÍNEAS SI LAS HABÍAS PUESTO:
    // private String email;
    // private String password;
}