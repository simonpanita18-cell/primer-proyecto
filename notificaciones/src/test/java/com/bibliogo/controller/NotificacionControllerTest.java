package com.bibliogo.Controller;

import com.bibliogo.notificaciones.controller.NotificacionController;
import com.bibliogo.notificaciones.model.Notificacion;
import com.bibliogo.notificaciones.service.NotificacionService;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(NotificacionController.class)   // ✅ AGREGADO — esto faltaba
public class NotificacionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private NotificacionService service;

    @Test
    void listarNotificaciones() throws Exception {

        Notificacion notificacion = new Notificacion();
        notificacion.setId(1);
        notificacion.setUsuarioId(1);
        notificacion.setTipo("devolucion");
        notificacion.setMensaje("Recuerda devolver tu libro pronto");
        notificacion.setEstado("pendiente");
        notificacion.setCreadoEn(LocalDateTime.now());

        when(service.listar()).thenReturn(List.of(notificacion));

        mockMvc.perform(get("/notificaciones/listar"))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$[0].tipo").value("devolucion"))
               .andExpect(jsonPath("$[0].estado").value("pendiente"));
    }

    @Test
    void buscarPorId() throws Exception {

        Notificacion notificacion = new Notificacion();
        notificacion.setId(2);
        notificacion.setUsuarioId(1);
        notificacion.setTipo("sistema");
        notificacion.setMensaje("Bienvenido a BiblioGo");
        notificacion.setEstado("leida");

        when(service.buscarPorId(2)).thenReturn(notificacion);

        mockMvc.perform(get("/notificaciones/2"))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.mensaje").value("Bienvenido a BiblioGo"));
    }

    @Test
    void buscarPendientesDeUsuario() throws Exception {

        Notificacion notificacion = new Notificacion();
        notificacion.setId(3);
        notificacion.setUsuarioId(1);
        notificacion.setEstado("pendiente");

        when(service.buscarNoLeidas(1)).thenReturn(List.of(notificacion));

        mockMvc.perform(get("/notificaciones/usuario/1/pendientes"))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$[0].estado").value("pendiente"));
    }
}