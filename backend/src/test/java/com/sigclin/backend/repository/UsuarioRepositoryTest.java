package com.sigclin.backend.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.sigclin.backend.model.Usuario;

@SpringBootTest // Carga Spring y usa H2 en memoria durante las pruebas
public class UsuarioRepositoryTest {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @BeforeEach
    void setUp() {
        // Limpia los datos para que cada prueba inicie en el mismo estado
        usuarioRepository.deleteAll();
    }

    @Test
    public void testGuardarUsuario() {
        Usuario usuario = new Usuario();
        usuario.setNombres("Fabricio");
        usuario.setEmail("fabricio.test@clinica.com");
        usuario.setPasswordHash("Clave123*");
        usuario.setEmailVerificado(false);

        // Valida que el repositorio pueda guardar un usuario y generar su ID
        Usuario usuarioGuardado = usuarioRepository.save(usuario);

        assertThat(usuarioGuardado).isNotNull();
        assertThat(usuarioGuardado.getIdUsuario()).isNotNull();
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

        // Valida la busqueda por email, flujo usado durante el inicio de sesion
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

        // Valida que findAll devuelva los usuarios guardados en la base de prueba
        List<Usuario> lista = usuarioRepository.findAll();

        assertThat(lista).isNotNull();
        assertThat(lista.size()).isEqualTo(2);
    }
}
