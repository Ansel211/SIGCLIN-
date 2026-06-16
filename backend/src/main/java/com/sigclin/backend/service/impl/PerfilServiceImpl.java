package com.sigclin.backend.service.impl;

import org.springframework.stereotype.Service;

import com.sigclin.backend.dto.perfil.NotificacionesRequest;
import com.sigclin.backend.dto.perfil.PerfilResponse;
import com.sigclin.backend.dto.perfil.PerfilUpdateRequest;
import com.sigclin.backend.model.Usuario;
import com.sigclin.backend.repository.UsuarioRepository;
import com.sigclin.backend.service.PerfilService;

@Service
public class PerfilServiceImpl implements PerfilService {

    private final UsuarioRepository usuarioRepository;
    private final PerfilMapper perfilMapper;

    public PerfilServiceImpl(UsuarioRepository usuarioRepository, PerfilMapper perfilMapper) {
        this.usuarioRepository = usuarioRepository;
        this.perfilMapper = perfilMapper;
    }

    @Override
    public PerfilResponse obtenerPerfil(Long idUsuario) {
        return perfilMapper.toResponse(buscarUsuario(idUsuario));
    }

    @Override
    public PerfilResponse actualizarPerfil(Long idUsuario, PerfilUpdateRequest request) {
        Usuario usuario = buscarUsuario(idUsuario);
        usuario.setNombres(request.getNombres());
        usuario.setApellidos(request.getApellidos());
        usuario.setFotoPerfilUrl(request.getFotoPerfilUrl());
        usuario.setPreferencias(request.getPreferencias());
        return perfilMapper.toResponse(usuarioRepository.save(usuario));
    }

    @Override
    public PerfilResponse configurarNotificaciones(Long idUsuario, NotificacionesRequest request) {
        Usuario usuario = buscarUsuario(idUsuario);
        usuario.setNotificacionesActivas(Boolean.TRUE.equals(request.getNotificacionesActivas()));
        return perfilMapper.toResponse(usuarioRepository.save(usuario));
    }

    private Usuario buscarUsuario(Long idUsuario) {
        return usuarioRepository.findById(idUsuario)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));
    }
}
