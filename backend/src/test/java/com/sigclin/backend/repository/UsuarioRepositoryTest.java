package com.sigclin.backend.repository;


import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.sigclin.backend.model.Usuario;

@SpringBootTest // Levanta la BD H2 en memoria para pruebas
public class UsuarioRepositoryTest {

    @Autowired
    private UsuarioRepository usuarioRepository; // repositorio real

    @BeforeEach
    void setUp() {
        usuarioRepository.deleteAll(); // Limpia la BD fantasma antes de cada test
    }

   
    @Test
    public void testGuardarUsuario() {
        Usuario usuario = new Usuario();
        usuario.setNombres("Fabricio");
        usuario.setEmail("fabricio.test@clinica.com");
        usuario.setPasswordHash("Clave123*");
        usuario.setEmailVerificado(false);

        // Guardamos en la base de datos H2
        Usuario usuarioGuardado = usuarioRepository.save(usuario);

        // Verificaciones al estilo de la clase
        assertThat(usuarioGuardado).isNotNull();
        assertThat(usuarioGuardado.getIdUsuario()).isNotNull(); // Verifica que genere ID automático
        assertThat(usuarioGuardado.getEmail()).isEqualTo("fabricio.test@clinica.com");
    }

    
    @Test
    public void testBuscarPorEmail() {
        Usuario usuario = new Usuario();
        usuario.setNombres("Angel");
        usuario.setEmail("angel.test@clinica.com");
        usuario.setPasswordHash("Clave456*");
        usuario.setEmailVerificado(true);
        usuarioRepository.save(usuario);

        // Ejecutamos el método que usa el Login para buscar usuarios
        Optional<Usuario> usuarioEncontrado = usuarioRepository.findByEmail("angel.test@clinica.com");

        assertThat(usuarioEncontrado.isPresent()).isTrue();
        assertThat(usuarioEncontrado.get().getPasswordHash()).isEqualTo("Clave456*");
    }

   
    @Test
    public void testFindAllUsuarios() {
        Usuario u1 = new Usuario();
        u1.setNombres("Usuario Uno");
        u1.setEmail("user1@clinica.com");
        u1.setPasswordHash("123");
        
        Usuario u2 = new Usuario();
        u2.setNombres("Usuario Dos");
        u2.setEmail("user2@clinica.com");
        u2.setPasswordHash("456");

        usuarioRepository.save(u1);
        usuarioRepository.save(u2);

        // Traemos la lista completa del H2 (igualito al findAll de tu profe)
        List<Usuario> lista = usuarioRepository.findAll();

        assertThat(lista).isNotNull();
        assertThat(lista.size()).isEqualTo(2); // Comprobamos que existan los 2 registros
    }
}