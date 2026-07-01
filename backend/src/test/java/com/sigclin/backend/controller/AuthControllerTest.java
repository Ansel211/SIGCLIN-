package com.sigclin.backend.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import com.sigclin.backend.dto.auth.AuthResponse;
import com.sigclin.backend.dto.auth.LoginRequest;
import com.sigclin.backend.dto.auth.RecuperarPasswordRequest;
import com.sigclin.backend.dto.auth.RegistroRequest;
import com.sigclin.backend.dto.perfil.PerfilResponse;
import com.sigclin.backend.service.AuthService;

@ExtendWith(MockitoExtension.class)
class AuthControllerTest {

    // Se simula el servicio para probar solo el comportamiento del controller
    @Mock
    private AuthService authService;

    private AuthController authController;

    @BeforeEach
    void setUp() {
        authController = new AuthController(authService);
    }

    // HU01: verifica que el endpoint de registro devuelva una sesion al crear usuario
    @Test
    void HU01_debeRegistrarUsuarioDesdeElFrontend() {
        RegistroRequest request = new RegistroRequest();
        request.setEmail("nuevo@sigclin.pe");
        request.setPassword("123456");
        request.setNombres("Nuevo");
        request.setApellidos("Usuario");

        AuthResponse esperado = AuthResponse.builder()
                .mensaje("Registrado correctamente")
                .tokenSesion("token-registro")
                .usuario(usuarioDemo())
                .build();

        when(authService.registrar(any(RegistroRequest.class))).thenReturn(esperado);

        ResponseEntity<AuthResponse> response = authController.registrar(request);

        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getTokenSesion()).isEqualTo("token-registro");
        assertThat(response.getBody().getUsuario().getEmail()).isEqualTo("usuario@sigclin.pe");
        verify(authService).registrar(request);
    }

    // HU02: valida que el controller responda correctamente ante credenciales validas
    @Test
    void HU02_debeIniciarSesionConCredencialesValidas() {
        LoginRequest request = new LoginRequest();
        request.setEmail("usuario@sigclin.pe");
        request.setPassword("123456");

        AuthResponse esperado = AuthResponse.builder()
                .mensaje("Inicio de sesion correcto")
                .tokenSesion("token-login")
                .usuario(usuarioDemo())
                .build();

        when(authService.login(any(LoginRequest.class))).thenReturn(esperado);

        ResponseEntity<AuthResponse> response = authController.login(request);

        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getMensaje()).contains("Inicio");
        assertThat(response.getBody().getTokenSesion()).isEqualTo("token-login");
        verify(authService).login(request);
    }

    // HU02: valida la solicitud de recuperacion de contrasena desde el controller
    @Test
    void HU02_debeSolicitarRecuperacionDePassword() {
        RecuperarPasswordRequest request = new RecuperarPasswordRequest();
        request.setEmail("usuario@sigclin.pe");

        when(authService.recuperarPassword(any(RecuperarPasswordRequest.class)))
                .thenReturn("Se genero token de recuperacion");

        ResponseEntity<String> response = authController.recuperarPassword(request);

        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
        assertThat(response.getBody()).contains("token");
        verify(authService).recuperarPassword(request);
    }

    // HU01: valida la confirmacion de email del usuario registrado
    @Test
    void HU01_debeVerificarEmailDelUsuarioRegistrado() {
        AuthResponse esperado = AuthResponse.builder()
                .mensaje("Email verificado")
                .tokenSesion("token-verificado")
                .usuario(usuarioDemo())
                .build();

        when(authService.verificarEmail(1L)).thenReturn(esperado);

        ResponseEntity<AuthResponse> response = authController.verificarEmail(1L);

        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getMensaje()).contains("Email");
        verify(authService).verificarEmail(1L);
    }

    // Respuesta reutilizable para evitar repetir datos del usuario en cada caso de prueba
    private PerfilResponse usuarioDemo() {
        return PerfilResponse.builder()
                .idUsuario(1L)
                .email("usuario@sigclin.pe")
                .nombres("Marco")
                .apellidos("Estudiante")
                .emailVerificado(true)
                .notificacionesActivas(true)
                .build();
    }
}
