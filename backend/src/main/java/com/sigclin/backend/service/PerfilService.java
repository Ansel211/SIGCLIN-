package com.sigclin.backend.service;

import com.sigclin.backend.dto.perfil.NotificacionesRequest;
import com.sigclin.backend.dto.perfil.PerfilResponse;
import com.sigclin.backend.dto.perfil.PerfilUpdateRequest;

public interface PerfilService {
    PerfilResponse obtenerPerfil(Long idUsuario);
    PerfilResponse actualizarPerfil(Long idUsuario, PerfilUpdateRequest request);
    PerfilResponse configurarNotificaciones(Long idUsuario, NotificacionesRequest request);
}
