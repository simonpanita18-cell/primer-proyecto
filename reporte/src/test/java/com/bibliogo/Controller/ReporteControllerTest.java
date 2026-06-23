package com.bibliogo.Controller;

import com.bibliogo.reporte.controller.ReporteController;
import com.bibliogo.reporte.model.Reporte;
import com.bibliogo.reporte.service.ReporteService;

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

@WebMvcTest(ReporteController.class)
public class ReporteControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ReporteService service;

    @Test
    void listarReportes() throws Exception {

        Reporte reporte = new Reporte();
        reporte.setId(1);
        reporte.setTipo("prestamos");
        reporte.setDatos("Reporte de préstamos activos");
        reporte.setGeneradoPor("admin");
        reporte.setGeneradoEn(LocalDateTime.now());

        when(service.listar()).thenReturn(List.of(reporte));

        mockMvc.perform(get("/reportes/listar"))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$[0].tipo").value("prestamos"))
               .andExpect(jsonPath("$[0].generadoPor").value("admin"));
    }

    @Test
    void buscarPorId() throws Exception {

        Reporte reporte = new Reporte();
        reporte.setId(2);
        reporte.setTipo("usuarios");
        reporte.setDatos("Reporte de usuarios activos");
        reporte.setGeneradoPor("admin");

        when(service.buscarPorId(2)).thenReturn(reporte);

        mockMvc.perform(get("/reportes/2"))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.tipo").value("usuarios"));
    }

    @Test
    void buscarPorTipo() throws Exception {

        Reporte reporte = new Reporte();
        reporte.setId(3);
        reporte.setTipo("prestamos");

        when(service.buscarPorTipo("prestamos")).thenReturn(List.of(reporte));

        mockMvc.perform(get("/reportes/tipo/prestamos"))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$[0].tipo").value("prestamos"));
    }
}