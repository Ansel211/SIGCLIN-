package com.sigclin.backend.dto.auth;

import com.sigclin.backend.dto.perfil.PerfilResponse;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthResponse {
    private String mensaje;
    private String tokenSesion;
    private PerfilResponse usuario;
}
