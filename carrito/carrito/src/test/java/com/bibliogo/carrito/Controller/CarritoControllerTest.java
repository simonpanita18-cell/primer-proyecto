package com.bibliogo.carrito.Controller;

import com.bibliogo.carrito.Model.Carrito;
import com.bibliogo.carrito.Service.CarritoService;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CarritoController.class)
public class CarritoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CarritoService service;

    @Test
    void listarCarrito() throws Exception {

        Carrito carrito = new Carrito();
        carrito.setId(1);
        carrito.setUsuarioId(1);
        carrito.setLibroId(2);
        carrito.setTituloLibro("Cien años de soledad");
        carrito.setCantidad(1);
        carrito.setEstado("activo");

        List<Carrito> carritos = List.of(carrito);

        when(service.listar()).thenReturn(carritos);

        mockMvc.perform(get("/carrito/listar"))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$[0].tituloLibro").value("Cien años de soledad"))
               .andExpect(jsonPath("$[0].estado").value("activo"));
    }

    @Test
    void buscarPorId() throws Exception {

        Carrito carrito = new Carrito();
        carrito.setId(2);
        carrito.setUsuarioId(1);
        carrito.setLibroId(3);
        carrito.setTituloLibro("1984");
        carrito.setCantidad(1);
        carrito.setEstado("confirmado");

        when(service.buscarPorId(2)).thenReturn(carrito);

        mockMvc.perform(get("/carrito/2"))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.tituloLibro").value("1984"));
    }

    @Test
    void carritoActivoDeUsuario() throws Exception {

        Carrito carrito = new Carrito();
        carrito.setId(3);
        carrito.setUsuarioId(1);
        carrito.setLibroId(1);
        carrito.setTituloLibro("El Principito");
        carrito.setCantidad(1);
        carrito.setEstado("activo");

        when(service.carritoActivoDeUsuario(1)).thenReturn(List.of(carrito));

        mockMvc.perform(get("/carrito/usuario/1/activo"))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$[0].estado").value("activo"));
    }
}