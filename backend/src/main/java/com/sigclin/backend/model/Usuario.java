package com.sigclin.backend.model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "usuario")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idUsuario;

    @Column(nullable = false, unique = true, length = 120)
    private String email;

    @Column(nullable = false)
    private String passwordHash;

    @Column(nullable = false, length = 120)
    private String nombres;

    @Column(length = 120)
    private String apellidos;

    @Column(columnDefinition = "TEXT")
    private String fotoPerfilUrl;

    @Column(length = 250)
    private String preferencias;

    @Builder.Default
    private Boolean notificacionesActivas = true;

    @Builder.Default
    private Boolean emailVerificado = false;

    @Column(length = 120)
    private String tokenSesion;

    @Column(length = 120)
    private String tokenRecuperacion;

    @Builder.Default
    private LocalDateTime fechaRegistro = LocalDateTime.now();
}
