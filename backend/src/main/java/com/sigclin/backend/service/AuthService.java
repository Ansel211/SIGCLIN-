package com.sigclin.backend.service;

import com.sigclin.backend.dto.auth.AuthResponse;
import com.sigclin.backend.dto.auth.LoginRequest;
import com.sigclin.backend.dto.auth.RecuperarPasswordRequest;
import com.sigclin.backend.dto.auth.RegistroRequest;

public interface AuthService {
    AuthResponse registrar(RegistroRequest request);
    AuthResponse login(LoginRequest request);
    String recuperarPassword(RecuperarPasswordRequest request);
    AuthResponse verificarEmail(Long idUsuario);
}
