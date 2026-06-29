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

import com.sigclin.backend.dto.perfil.NotificacionesRequest;
import com.sigclin.backend.dto.perfil.PerfilResponse;
import com.sigclin.backend.dto.perfil.PerfilUpdateRequest;
import com.sigclin.backend.service.PerfilService;

@ExtendWith(MockitoExtension.class)
class PerfilControllerTest {

    // Se simula PerfilService para probar el controller sin depender de la base de datos
    @Mock
    private PerfilService perfilService;

    private PerfilController perfilController;

    @BeforeEach
    void setUp() {
        perfilController = new PerfilController(perfilService);
    }

    // HU03: valida la consulta del perfil del usuario autenticado.
    @Test
    void HU03_debeObtenerPerfilDelUsuarioAutenticado() {
        when(perfilService.obtenerPerfil(1L)).thenReturn(perfilDemo());

        ResponseEntity<PerfilResponse> response = perfilController.obtenerPerfil(1L);

        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getEmail()).isEqualTo("usuario@sigclin.pe");
        verify(perfilService).obtenerPerfil(1L);
    }

    // HU03: valida que el controller reciba y devuelva los datos actualizados del perfil
    @Test
    void HU03_debeActualizarDatosDelPerfil() {
        PerfilUpdateRequest request = new PerfilUpdateRequest();
        request.setNombres("Marco Antonio");
        request.setApellidos("Estudiante");
        request.setFotoPerfilUrl("data:image/png;base64,AAA");
        request.setPreferencias("Recordatorio por correo");

        PerfilResponse actualizado = perfilDemo();
        actualizado.setNombres("Marco Antonio");
        actualizado.setPreferencias("Recordatorio por correo");

        when(perfilService.actualizarPerfil(any(Long.class), any(PerfilUpdateRequest.class)))
                .thenReturn(actualizado);

        ResponseEntity<PerfilResponse> response = perfilController.actualizarPerfil(1L, request);

        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getNombres()).isEqualTo("Marco Antonio");
        assertThat(response.getBody().getPreferencias()).isEqualTo("Recordatorio por correo");
        verify(perfilService).actualizarPerfil(1L, request);
    }

    // HU03: valida la activacion o desactivacion de notificaciones del perfil
    @Test
    void HU03_debeConfigurarNotificacionesDelPerfil() {
        NotificacionesRequest request = new NotificacionesRequest();
        request.setNotificacionesActivas(false);

        PerfilResponse actualizado = perfilDemo();
        actualizado.setNotificacionesActivas(false);

        when(perfilService.configurarNotificaciones(any(Long.class), any(NotificacionesRequest.class)))
                .thenReturn(actualizado);

        ResponseEntity<PerfilResponse> response = perfilController.configurarNotificaciones(1L, request);

        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getNotificacionesActivas()).isFalse();
        verify(perfilService).configurarNotificaciones(1L, request);
    }

    // Datos base para reutilizar la misma estructura de perfil en los casos de prueba
    private PerfilResponse perfilDemo() {
        return PerfilResponse.builder()
                .idUsuario(1L)
                .email("usuario@sigclin.pe")
                .nombres("Marco")
                .apellidos("Estudiante")
                .fotoPerfilUrl("data:image/png;base64,AAA")
                .preferencias("Atencion por correo")
                .notificacionesActivas(true)
                .emailVerificado(true)
                .build();
    }
}
