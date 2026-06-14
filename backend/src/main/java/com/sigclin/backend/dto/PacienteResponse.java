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
public class PacienteResponse {

    private Long idPaciente; // <-- ¡Clave! El Frontend necesita el ID para editar o eliminar
    private String nombres;
    private String apellidoPaterno;
    private String apellidoMaterno;
    private String numDocumento;
    private String telefono;
    private String direccion;
    private LocalDate fechaNacimiento;
    
    // Aquí puedes meter campos de control que le sirvan al Frontend, por ejemplo:
    // private Boolean activo; 
    // private String nombreCompleto; // (puedes juntar nombres + apellidos en el service)
}