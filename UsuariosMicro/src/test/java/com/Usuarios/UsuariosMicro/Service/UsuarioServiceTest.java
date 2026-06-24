package com.Usuarios.UsuariosMicro.Service;

import com.Usuarios.UsuariosMicro.Exception.ConflictoException;
import com.Usuarios.UsuariosMicro.Exception.RecursoNoEncontradoException;
import com.Usuarios.UsuariosMicro.Model.Usuario;
import com.Usuarios.UsuariosMicro.Repository.UsuarioRepository;
import com.Usuarios.UsuariosMicro.dto.UsuarioRequestDTO;
import com.Usuarios.UsuariosMicro.dto.UsuarioResponseDTO;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UsuarioServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UsuarioService usuarioService;

    @Test
    void listarTodos_debeRetornarListaDeUsuarios() {
        Usuario usuario = new Usuario();
        usuario.setId(1);
        usuario.setNombre("Simon");
        usuario.setCorreo("simon@bibliogo.com");

        when(usuarioRepository.findAll()).thenReturn(List.of(usuario));

        List<UsuarioResponseDTO> resultado = usuarioService.listarTodos();

        assertEquals(1, resultado.size());
        assertEquals("Simon", resultado.get(0).getNombre());
    }

    @Test
    void buscarPorId_cuandoExiste_debeRetornarUsuario() {
        Usuario usuario = new Usuario();
        usuario.setId(1);
        usuario.setNombre("Simon");

        when(usuarioRepository.findById(1)).thenReturn(Optional.of(usuario));

        UsuarioResponseDTO resultado = usuarioService.buscarPorId(1);

        assertEquals("Simon", resultado.getNombre());
    }

    @Test
    void buscarPorId_cuandoNoExiste_debeLanzarExcepcion() {
        when(usuarioRepository.findById(99)).thenReturn(Optional.empty());

        assertThrows(RecursoNoEncontradoException.class,
                () -> usuarioService.buscarPorId(99));
    }

    @Test
    void crear_conCorreoNuevo_debeCrearUsuario() {
        UsuarioRequestDTO dto = new UsuarioRequestDTO();
        dto.setNombre("Simon");
        dto.setApellido("Hercules");
        dto.setCorreo("simon@bibliogo.com");
        dto.setPassword("123456");
        dto.setRol("LECTOR");

        when(usuarioRepository.findByCorreoIgnoreCase(dto.getCorreo()))
                .thenReturn(Optional.empty());
        when(passwordEncoder.encode(dto.getPassword())).thenReturn("encriptada");

        Usuario guardado = new Usuario();
        guardado.setId(1);
        guardado.setNombre("Simon");
        guardado.setCorreo("simon@bibliogo.com");
        guardado.setEstado("activo");

        when(usuarioRepository.save(any(Usuario.class))).thenReturn(guardado);

        UsuarioResponseDTO resultado = usuarioService.crear(dto);

        assertEquals("Simon", resultado.getNombre());
        assertEquals("activo", resultado.getEstado());
        verify(usuarioRepository, times(1)).save(any(Usuario.class));
    }

    @Test
    void crear_conCorreoExistente_debeLanzarConflicto() {
        UsuarioRequestDTO dto = new UsuarioRequestDTO();
        dto.setCorreo("simon@bibliogo.com");

        when(usuarioRepository.findByCorreoIgnoreCase(dto.getCorreo()))
                .thenReturn(Optional.of(new Usuario()));

        assertThrows(ConflictoException.class,
                () -> usuarioService.crear(dto));

        verify(usuarioRepository, never()).save(any());
    }

    @Test
    void eliminar_cuandoExiste_debeEliminarCorrectamente() {
        when(usuarioRepository.existsById(1)).thenReturn(true);

        usuarioService.eliminar(1);

        verify(usuarioRepository, times(1)).deleteById(1);
    }

    @Test
    void eliminar_cuandoNoExiste_debeLanzarExcepcion() {
        when(usuarioRepository.existsById(99)).thenReturn(false);

        assertThrows(RecursoNoEncontradoException.class,
                () -> usuarioService.eliminar(99));

        verify(usuarioRepository, never()).deleteById(any());
    }
}