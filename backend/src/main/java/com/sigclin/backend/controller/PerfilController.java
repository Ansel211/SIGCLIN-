package com.sigclin.backend.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sigclin.backend.dto.perfil.NotificacionesRequest;
import com.sigclin.backend.dto.perfil.PerfilResponse;
import com.sigclin.backend.dto.perfil.PerfilUpdateRequest;
import com.sigclin.backend.service.PerfilService;

@RestController
@RequestMapping("/api/v1/perfil")
public class PerfilController {

    private final PerfilService perfilService;

    public PerfilController(PerfilService perfilService) {
        this.perfilService = perfilService;
    }

    @GetMapping("/{idUsuario}")
    public ResponseEntity<PerfilResponse> obtenerPerfil(@PathVariable Long idUsuario) {
        return ResponseEntity.ok(perfilService.obtenerPerfil(idUsuario));
    }

    @PutMapping("/{idUsuario}")
    public ResponseEntity<PerfilResponse> actualizarPerfil(
            @PathVariable Long idUsuario, //sirve para capturar el id a traves de la url del navegador 
            @RequestBody PerfilUpdateRequest request) { //RequestBody obliga agarrar el cuerpo del jason y meterlo dentro del objeto java 
        return ResponseEntity.ok(perfilService.actualizarPerfil(idUsuario, request));
    }

    @PutMapping("/{idUsuario}/notificaciones")
    public ResponseEntity<PerfilResponse> configurarNotificaciones(
            @PathVariable Long idUsuario,
            @RequestBody NotificacionesRequest request) {
        return ResponseEntity.ok(perfilService.configurarNotificaciones(idUsuario, request));
    }
}
