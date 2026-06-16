package com.sigclin.backend.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
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
import com.sigclin.backend.model.Paciente;
import com.sigclin.backend.repository.PacienteRepository;

@RestController
@RequestMapping("/api/v1/paciente")
public class PacienteController {

    private final PacienteRepository pacienteRepository;

    public PacienteController(PacienteRepository pacienteRepository) {
        this.pacienteRepository = pacienteRepository;
    }

    @GetMapping
    public ResponseEntity<List<PacienteResponse>> listarPacientes() {
        return ResponseEntity.ok(pacienteRepository.findAll().stream()
                .map(this::toResponse)
                .toList());
    }

    @PostMapping
    public ResponseEntity<PacienteResponse> registrarPaciente(@RequestBody PacienteRequest request) {
        Paciente paciente = new Paciente();
        applyRequest(paciente, request);
        if (paciente.getEstado() == null) {
            paciente.setEstado("ACTIVO");
        }
        return ResponseEntity.ok(toResponse(pacienteRepository.save(paciente)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PacienteResponse> actualizarPaciente(@PathVariable Long id, @RequestBody PacienteRequest request) {
        Paciente paciente = pacienteRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Paciente no encontrado"));
        applyRequest(paciente, request);
        return ResponseEntity.ok(toResponse(pacienteRepository.save(paciente)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminarPaciente(@PathVariable Long id) {
        pacienteRepository.deleteById(id);
        return ResponseEntity.ok("Paciente eliminado del sistema");
    }

    private void applyRequest(Paciente paciente, PacienteRequest request) {
        paciente.setDni(request.getDni());
        paciente.setNombres(request.getNombres());
        paciente.setApellidoPaterno(request.getApellidoPaterno());
        paciente.setApellidoMaterno(request.getApellidoMaterno());
        paciente.setNumDocumento(request.getNumDocumento());
        paciente.setTelefono(request.getTelefono());
        paciente.setDireccion(request.getDireccion());
        paciente.setFechaNacimiento(request.getFechaNacimiento());
        paciente.setEstado(request.getEstado());
    }

    private PacienteResponse toResponse(Paciente paciente) {
        return PacienteResponse.builder()
                .idPaciente(paciente.getIdPaciente())
                .dni(paciente.getDni())
                .nombres(paciente.getNombres())
                .apellidoPaterno(paciente.getApellidoPaterno())
                .apellidoMaterno(paciente.getApellidoMaterno())
                .numDocumento(paciente.getNumDocumento())
                .telefono(paciente.getTelefono())
                .direccion(paciente.getDireccion())
                .fechaNacimiento(paciente.getFechaNacimiento())
                .estado(paciente.getEstado())
                .build();
    }
}
