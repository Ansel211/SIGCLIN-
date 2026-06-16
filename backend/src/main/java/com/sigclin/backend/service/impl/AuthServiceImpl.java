package com.sigclin.backend.service.impl;

import java.util.UUID;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.sigclin.backend.dto.auth.AuthResponse;
import com.sigclin.backend.dto.auth.LoginRequest;
import com.sigclin.backend.dto.auth.RecuperarPasswordRequest;
import com.sigclin.backend.dto.auth.RegistroRequest;
import com.sigclin.backend.model.Usuario;
import com.sigclin.backend.repository.UsuarioRepository;
import com.sigclin.backend.service.AuthService;

@Service
public class AuthServiceImpl implements AuthService {

    private final UsuarioRepository usuarioRepository;
    private final PerfilMapper perfilMapper;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public AuthServiceImpl(UsuarioRepository usuarioRepository, PerfilMapper perfilMapper) {
        this.usuarioRepository = usuarioRepository;
        this.perfilMapper = perfilMapper;
    }

    @Override
    public AuthResponse registrar(RegistroRequest request) {
        if (usuarioRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("El email ya se encuentra registrado");
        }

        Usuario usuario = Usuario.builder()
                .email(request.getEmail())
                .passwordHash(passwordEncoder.encode(request.getPassword()))
                .nombres(request.getNombres())
                .apellidos(request.getApellidos())
                .tokenSesion(UUID.randomUUID().toString())
                .build();

        Usuario guardado = usuarioRepository.save(usuario);
        return AuthResponse.builder()
                .mensaje("Usuario registrado correctamente")
                .tokenSesion(guardado.getTokenSesion())
                .usuario(perfilMapper.toResponse(guardado))
                .build();
    }

    @Override
    public AuthResponse login(LoginRequest request) {
        Usuario usuario = usuarioRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("Credenciales no validas"));

        if (!passwordEncoder.matches(request.getPassword(), usuario.getPasswordHash())) {
            throw new IllegalArgumentException("Credenciales no validas");
        }

        usuario.setTokenSesion(UUID.randomUUID().toString());
        Usuario actualizado = usuarioRepository.save(usuario);

        return AuthResponse.builder()
                .mensaje("Inicio de sesion correcto")
                .tokenSesion(actualizado.getTokenSesion())
                .usuario(perfilMapper.toResponse(actualizado))
                .build();
    }

    @Override
    public String recuperarPassword(RecuperarPasswordRequest request) {
        Usuario usuario = usuarioRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("No existe usuario con ese email"));
        usuario.setTokenRecuperacion(UUID.randomUUID().toString());
        usuarioRepository.save(usuario);
        return "Se genero token de recuperacion. En produccion se enviaria por correo electronico.";
    }

    @Override
    public AuthResponse verificarEmail(Long idUsuario) {
        Usuario usuario = usuarioRepository.findById(idUsuario)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));
        usuario.setEmailVerificado(true);
        Usuario actualizado = usuarioRepository.save(usuario);
        return AuthResponse.builder()
                .mensaje("Email verificado correctamente")
                .tokenSesion(actualizado.getTokenSesion())
                .usuario(perfilMapper.toResponse(actualizado))
                .build();
    }
}
