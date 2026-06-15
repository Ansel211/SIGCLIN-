package com.sigclin.backend.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
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

import com.sigclin.backend.model.Paciente; // ¡AJUSTADO!: Usa tu ruta real (.model.Paciente)
import com.sigclin.backend.repository.PacienteRepository;

@RestController
@RequestMapping("/api/v1/paciente")
@CrossOrigin(origins = "*")
public class PacienteController {

    @Autowired
    private PacienteRepository pacienteRepository; // El cable a tu repositorio existente

    // 1. GET - Listar todos los pacientes reales de tu pgAdmin
    @GetMapping
    public ResponseEntity<List<Paciente>> listarPacientes() {
        List<Paciente> listaReal = pacienteRepository.findAll();
        return ResponseEntity.ok(listaReal);
    }

    // 2. POST - Registrar y guardar de verdad en la Base de Datos
    @PostMapping
    public ResponseEntity<Paciente> registrarPaciente(@RequestBody Paciente paciente) {
        Paciente pacienteGuardado = pacienteRepository.save(paciente);
        return ResponseEntity.ok(pacienteGuardado); // Devuelve el paciente con su ID real
    }

    // 3. PUT - Modificar perfil en la BD real
    @PutMapping("/{id}")
    public ResponseEntity<Paciente> actualizarPaciente(@PathVariable Long id, @RequestBody Paciente datosNuevos) {
        return pacienteRepository.findById(id).map(pacienteExistente -> {
            pacienteExistente.setNombres(datosNuevos.getNombres());
            pacienteExistente.setApellidoPaterno(datosNuevos.getApellidoPaterno());
            pacienteExistente.setDni(datosNuevos.getDni());
            // Si tienes más campos en tu modelo (como telefono o direccion), agrégalos aquí abajo igual que los de arriba:
            // pacienteExistente.setTelefono(datosNuevos.getTelefono());
            
            Paciente actualizado = pacienteRepository.save(pacienteExistente);
            return ResponseEntity.ok(actualizado);
        }).orElse(ResponseEntity.notFound().build());
    }

    // 4. DELETE - Eliminar de la BD real usando el ID
    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminarPaciente(@PathVariable Long id) {
        pacienteRepository.deleteById(id);
        return ResponseEntity.ok("Paciente eliminado de la base de datos con éxito");
    }
}