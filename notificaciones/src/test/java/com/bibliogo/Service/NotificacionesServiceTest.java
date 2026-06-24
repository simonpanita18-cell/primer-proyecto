package com.bibliogo.Service;

import com.bibliogo.notificaciones.dto.NotificacionRequestDTO;
import com.bibliogo.notificaciones.dto.NotificacionResponseDTO;
import com.bibliogo.notificaciones.exception.ConflictoException;
import com.bibliogo.notificaciones.exception.RecursoNoEncontradoException;
import com.bibliogo.notificaciones.model.Notificacion;
import com.bibliogo.notificaciones.repository.NotificacionRepository;
import com.bibliogo.notificaciones.service.NotificacionService;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class NotificacionServiceTest {

    @Mock
    private NotificacionRepository repository;

    @InjectMocks
    private NotificacionService notificacionService;

    @Test
    void listar_debeRetornarListaDeNotificaciones() {
        Notificacion notificacion = new Notificacion();
        notificacion.setId(1);
        notificacion.setTipo("devolucion");
        notificacion.setEstado("pendiente");

        when(repository.findAll()).thenReturn(List.of(notificacion));

        List<Notificacion> resultado = notificacionService.listar();

        assertEquals(1, resultado.size());
        assertEquals("devolucion", resultado.get(0).getTipo());
    }

    @Test
    void buscarPorId_cuandoExiste_debeRetornarNotificacion() {
        Notificacion notificacion = new Notificacion();
        notificacion.setId(1);
        notificacion.setMensaje("Bienvenido a BiblioGo");

        when(repository.findById(1)).thenReturn(Optional.of(notificacion));

        Notificacion resultado = notificacionService.buscarPorId(1);

        assertEquals("Bienvenido a BiblioGo", resultado.getMensaje());
    }

    @Test
    void buscarPorId_cuandoNoExiste_debeLanzarExcepcion() {
        when(repository.findById(99)).thenReturn(Optional.empty());

        assertThrows(RecursoNoEncontradoException.class,
                () -> notificacionService.buscarPorId(99));
    }

    @Test
    void buscarNoLeidas_debeRetornarSoloLasPendientes() {
        Notificacion notificacion = new Notificacion();
        notificacion.setId(1);
        notificacion.setEstado("pendiente");

        when(repository.findByUsuarioIdAndEstado(1, "pendiente"))
                .thenReturn(List.of(notificacion));

        List<Notificacion> resultado = notificacionService.buscarNoLeidas(1);

        assertEquals(1, resultado.size());
        assertEquals("pendiente", resultado.get(0).getEstado());
    }

    @Test
    void crear_debeCrearNotificacionConEstadoPendiente() {
        NotificacionRequestDTO dto = new NotificacionRequestDTO();
        dto.setUsuarioId(1);
        dto.setTipo("sistema");
        dto.setMensaje("Bienvenido a BiblioGo");

        Notificacion guardada = new Notificacion();
        guardada.setId(1);
        guardada.setUsuarioId(1);
        guardada.setTipo("sistema");
        guardada.setMensaje("Bienvenido a BiblioGo");
        guardada.setEstado("pendiente");
        guardada.setCreadoEn(LocalDateTime.now());

        when(repository.save(any(Notificacion.class))).thenReturn(guardada);

        NotificacionResponseDTO resultado = notificacionService.crear(dto);

        assertEquals("sistema", resultado.getTipo());
        assertEquals("pendiente", resultado.getEstado());
    }

    @Test
    void marcarComoLeida_conEstadoPendiente_debeCambiarALeida() {
        Notificacion notificacion = new Notificacion();
        notificacion.setId(1);
        notificacion.setEstado("pendiente");

        when(repository.findById(1)).thenReturn(Optional.of(notificacion));
        when(repository.save(any(Notificacion.class))).thenAnswer(invocation -> invocation.getArgument(0));

        NotificacionResponseDTO resultado = notificacionService.marcarComoLeida(1);

        assertEquals("leida", resultado.getEstado());
    }

    @Test
    void marcarComoLeida_cuandoYaEstaLeida_debeLanzarConflicto() {
        Notificacion notificacion = new Notificacion();
        notificacion.setId(1);
        notificacion.setEstado("leida");

        when(repository.findById(1)).thenReturn(Optional.of(notificacion));

        assertThrows(ConflictoException.class,
                () -> notificacionService.marcarComoLeida(1));

        verify(repository, never()).save(any());
    }

    @Test
    void eliminar_cuandoExiste_debeEliminarCorrectamente() {
        when(repository.existsById(1)).thenReturn(true);

        notificacionService.eliminar(1);

        verify(repository, times(1)).deleteById(1);
    }

    @Test
    void eliminar_cuandoNoExiste_debeLanzarExcepcion() {
        when(repository.existsById(99)).thenReturn(false);

        assertThrows(RecursoNoEncontradoException.class,
                () -> notificacionService.eliminar(99));
    }
}