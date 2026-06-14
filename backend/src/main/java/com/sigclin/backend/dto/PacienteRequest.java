package com.sigclin.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PacienteRequest {

    private String nombres;
    private String apellidoPaterno;
    private String apellidoMaterno;
    private String numDocumento;
    private String telefono;
    private String direccion;
    private LocalDate fechaNacimiento;
    
    // Aquí puedes agregar campos específicos de TU tabla paciente si los tienes, por ejemplo:
    // private String nroHistoriaClinica;
    // private String tipoSeguro;
}