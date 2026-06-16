package com.sigclin.backend.dto.perfil;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PerfilResponse {
    private Long idUsuario;
    private String email;
    private String nombres;
    private String apellidos;
    private String fotoPerfilUrl;
    private String preferencias;
    private Boolean notificacionesActivas;
    private Boolean emailVerificado;
}
