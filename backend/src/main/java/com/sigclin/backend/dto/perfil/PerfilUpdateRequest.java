package com.sigclin.backend.dto.perfil;

import lombok.Data;

@Data
public class PerfilUpdateRequest {
    private String nombres;
    private String apellidos;
    private String fotoPerfilUrl;
    private String preferencias;
}
