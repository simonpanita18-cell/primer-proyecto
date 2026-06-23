package com.bibliogo.envio.controller;

import com.bibliogo.envio.model.Envio;
import com.bibliogo.envio.service.EnvioService;

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

@WebMvcTest(EnvioController.class)
public class EnvioControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private EnvioService service;

    @Test
    void listarEnvios() throws Exception {

        Envio envio = new Envio();
        envio.setId(1);
        envio.setPrestamoId(1);
        envio.setUsuarioId(1);
        envio.setDireccion("Av. Principal 123");
        envio.setComuna("Quilicura");
        envio.setEstado("entregado");
        envio.setFechaCreacion(LocalDateTime.now());

        when(service.listar()).thenReturn(List.of(envio));

        mockMvc.perform(get("/envios/listar"))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$[0].comuna").value("Quilicura"))
               .andExpect(jsonPath("$[0].estado").value("entregado"));
    }

    @Test
    void buscarPorId() throws Exception {

        Envio envio = new Envio();
        envio.setId(2);
        envio.setPrestamoId(2);
        envio.setUsuarioId(1);
        envio.setDireccion("Av. Siempre Viva 123");
        envio.setComuna("Santiago");
        envio.setEstado("pendiente");

        when(service.buscarPorId(2)).thenReturn(envio);

        mockMvc.perform(get("/envios/2"))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.comuna").value("Santiago"));
    }

    @Test
    void buscarPorEstado() throws Exception {

        Envio envio = new Envio();
        envio.setId(3);
        envio.setEstado("en camino");

        when(service.buscarPorEstado("en camino")).thenReturn(List.of(envio));

        mockMvc.perform(get("/envios/estado/en camino"))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$[0].estado").value("en camino"));
    }
}