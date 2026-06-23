package com.bibliogo.resena.controller;

import com.bibliogo.resena.controller.ResenaController;
import com.bibliogo.resena.model.Resena;
import com.bibliogo.resena.service.ResenaService;

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

@WebMvcTest(ResenaController.class)
public class ResenaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ResenaService service;

    @Test
    void listarResenas() throws Exception {

        Resena resena = new Resena();
        resena.setId(1);
        resena.setUsuarioId(1);
        resena.setLibroId(1);
        resena.setCalificacion(5);
        resena.setComentario("Muy bueno");
        resena.setCreadoEn(LocalDateTime.now());

        when(service.listar()).thenReturn(List.of(resena));

        mockMvc.perform(get("/resenas/listar"))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$[0].comentario").value("Muy bueno"))
               .andExpect(jsonPath("$[0].calificacion").value(5));
    }

    @Test
    void buscarPorId() throws Exception {

        Resena resena = new Resena();
        resena.setId(2);
        resena.setUsuarioId(1);
        resena.setLibroId(1);
        resena.setCalificacion(4);
        resena.setComentario("Buena lectura");

        when(service.buscarPorId(2)).thenReturn(resena);

        mockMvc.perform(get("/resenas/2"))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.calificacion").value(4));
    }

    @Test
    void buscarPorLibro() throws Exception {

        Resena resena = new Resena();
        resena.setId(3);
        resena.setLibroId(1);
        resena.setCalificacion(5);

        when(service.buscarPorLibro(1)).thenReturn(List.of(resena));

        mockMvc.perform(get("/resenas/libro/1"))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$[0].calificacion").value(5));
    }
}