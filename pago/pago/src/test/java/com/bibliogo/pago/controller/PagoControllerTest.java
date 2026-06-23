package com.bibliogo.pago.controller;

import com.bibliogo.pago.model.Pago;
import com.bibliogo.pago.service.PagoService;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PagoController.class)
public class PagoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private PagoService service;

    @Test
    void listarPagos() throws Exception {

        Pago pago = new Pago();
        pago.setId(1);
        pago.setPrestamoId(1);
        pago.setUsuarioId(1);
        pago.setMonto(new BigDecimal("2500.00"));
        pago.setMetodo("tarjeta");
        pago.setEstado("pendiente");
        pago.setTipo("multa");
        pago.setFechaPago(LocalDateTime.now());

        when(service.listar()).thenReturn(List.of(pago));

        mockMvc.perform(get("/pagos/listar"))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$[0].tipo").value("multa"))
               .andExpect(jsonPath("$[0].estado").value("pendiente"));
    }

    @Test
    void buscarPorId() throws Exception {

        Pago pago = new Pago();
        pago.setId(2);
        pago.setPrestamoId(1);
        pago.setUsuarioId(1);
        pago.setMonto(new BigDecimal("2500.00"));
        pago.setMetodo("tarjeta");
        pago.setEstado("pagado");
        pago.setTipo("multa");

        when(service.buscarPorId(2)).thenReturn(pago);

        mockMvc.perform(get("/pagos/2"))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.estado").value("pagado"));
    }

    @Test
    void buscarPorEstado() throws Exception {

        Pago pago = new Pago();
        pago.setId(3);
        pago.setEstado("pendiente");

        when(service.buscarPorEstado("pendiente")).thenReturn(List.of(pago));

        mockMvc.perform(get("/pagos/estado/pendiente"))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$[0].estado").value("pendiente"));
    }
}