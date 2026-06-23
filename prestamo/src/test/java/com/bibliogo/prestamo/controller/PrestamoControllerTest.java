package com.bibliogo.prestamo.controller;

import com.bibliogo.prestamo.dto.PrestamoResponseDTO;
import com.bibliogo.prestamo.service.PrestamoService;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PrestamoController.class)
public class PrestamoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private PrestamoService service;

    @Test
    void listarPrestamos() throws Exception {

        PrestamoResponseDTO prestamo = new PrestamoResponseDTO(
                1, 1, 2, "Cien años de soledad",
                LocalDate.now(), LocalDate.now().plusDays(7), null, "activo"
        );

        when(service.listar()).thenReturn(List.of(prestamo));

        mockMvc.perform(get("/prestamos/listar"))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$[0].tituloLibro").value("Cien años de soledad"))
               .andExpect(jsonPath("$[0].estado").value("activo"));
    }

    @Test
    void buscarPorId() throws Exception {

        PrestamoResponseDTO prestamo = new PrestamoResponseDTO(
                2, 1, 3, "1984",
                LocalDate.now(), LocalDate.now().plusDays(7), null, "activo"
        );

        when(service.buscarPorId(2)).thenReturn(prestamo);

        mockMvc.perform(get("/prestamos/2"))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.tituloLibro").value("1984"));
    }

    @Test
    void prestamosActivosDeUsuario() throws Exception {

        PrestamoResponseDTO prestamo = new PrestamoResponseDTO(
                3, 1, 1, "El Principito",
                LocalDate.now(), LocalDate.now().plusDays(7), null, "activo"
        );

        when(service.prestamosActivosDeUsuario(1)).thenReturn(List.of(prestamo));

        mockMvc.perform(get("/prestamos/usuario/1/activos"))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$[0].estado").value("activo"));
    }
}