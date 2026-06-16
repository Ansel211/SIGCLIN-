package com.sigclin.backend.service.impl;

import org.springframework.stereotype.Component;

import com.sigclin.backend.dto.perfil.PerfilResponse;
import com.sigclin.backend.model.Usuario;

@Component
public class PerfilMapper {
    public PerfilResponse toResponse(Usuario usuario) {
        return PerfilResponse.builder()
                .idUsuario(usuario.getIdUsuario())
                .email(usuario.getEmail())
                .nombres(usuario.getNombres())
                .apellidos(usuario.getApellidos())
                .fotoPerfilUrl(usuario.getFotoPerfilUrl())
                .preferencias(usuario.getPreferencias())
                .notificacionesActivas(usuario.getNotificacionesActivas())
                .emailVerificado(usuario.getEmailVerificado())
                .build();
    }
}
