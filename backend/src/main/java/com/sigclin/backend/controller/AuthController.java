package com.sigclin.backend.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sigclin.backend.dto.auth.AuthResponse;
import com.sigclin.backend.dto.auth.LoginRequest;
import com.sigclin.backend.dto.auth.RecuperarPasswordRequest;
import com.sigclin.backend.dto.auth.RegistroRequest;
import com.sigclin.backend.service.AuthService;

@RestController
@RequestMapping("/api/v1/auth") //los mapping indican que el metodo solo se activa cuando recibe una peticion http de tipo... 
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/registro")
    public ResponseEntity<AuthResponse> registrar(@RequestBody RegistroRequest request) {
        return ResponseEntity.ok(authService.registrar(request));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }

    @PostMapping("/recuperar-password")
    public ResponseEntity<String> recuperarPassword(@RequestBody RecuperarPasswordRequest request) {
        return ResponseEntity.ok(authService.recuperarPassword(request));
    }

    @PostMapping("/verificar-email/{idUsuario}")
    public ResponseEntity<AuthResponse> verificarEmail(@PathVariable Long idUsuario) {
        return ResponseEntity.ok(authService.verificarEmail(idUsuario));
    }
}
