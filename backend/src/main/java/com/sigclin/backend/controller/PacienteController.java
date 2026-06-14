package com.sigclin.backend.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sigclin.backend.dto.PacienteRequest;
import com.sigclin.backend.dto.PacienteResponse;

@RestController
@RequestMapping("/api/v1/paciente") // El estándar /api/v1 que usa tu profe
@CrossOrigin(origins = "*")
public class PacienteController {

    // 1. GET - Listar todos los pacientes
    @GetMapping
    public ResponseEntity<List<PacienteResponse>> listarPacientes() {
        PacienteResponse ejemplo = PacienteResponse.builder()
                .idPaciente(1L)
                .nombres("Juan")
                .apellidoPaterno("Pérez")
                .numDocumento("77665544")
                .build();
        return ResponseEntity.ok(List.of(ejemplo));
    }

    // 2. POST - Registrar un paciente (Este cubre el "Registro" de tu Sprint)
    @PostMapping
    public ResponseEntity<String> registrarPaciente(@RequestBody PacienteRequest request) {
        return ResponseEntity.ok("Paciente registrado exitosamente en el sistema");
    }

    // 3. PUT - Modificar perfil (Este cubre la "Gestión de Perfil" de tu Sprint)
    @PutMapping("/{id}")
    public ResponseEntity<String> actualizarPaciente(@PathVariable Long id, @RequestBody PacienteRequest request) {
        return ResponseEntity.ok("Paciente con ID " + id + " actualizado correctamente");
    }

    // 4. DELETE - Eliminar paciente
    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminarPaciente(@PathVariable Long id) {
        return ResponseEntity.ok("Paciente eliminado del sistema");
    }
}