package com.bibliogo.catalogo_service.controller;

import com.bibliogo.catalogo_service.Controller.LibroController;
import com.bibliogo.catalogo_service.Model.Libro;
import com.bibliogo.catalogo_service.Service.LibroService;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
//que en listar libros devuelva un 201 iscreated
@WebMvcTest(LibroController.class)
public class LibroControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private LibroService service;

    @Test
    void listarLibros() throws Exception {

        Libro libro = new Libro();
        libro.setId(1);
        libro.setTitulo("Cien años de soledad");
        libro.setAutor("Gabriel García Márquez");
        libro.setCategoria("Novela");
        libro.setIsbn("978-84-01-02");
        libro.setStock(3);
        libro.setDisponibilidad("disponible");

        List<Libro> libros = List.of(libro);

        when(service.listar()).thenReturn(libros);

        mockMvc.perform(get("/libros/listar"))
               .andExpect(status().isCreated())
               .andExpect(jsonPath("$[0].titulo").value("Cien años de soledad"))
               .andExpect(jsonPath("$[0].autor").value("Gabriel García Márquez"));
    }

    @Test
    void buscarPorId() throws Exception {

        Libro libro = new Libro();
        libro.setId(1);
        libro.setTitulo("1984");
        libro.setAutor("George Orwell");
        libro.setStock(5);
        libro.setDisponibilidad("disponible");

        when(service.buscarPorId(1)).thenReturn(libro);

        mockMvc.perform(get("/libros/1"))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.titulo").value("1984"));
    }

    @Test
    void buscarLibrosDisponibles() throws Exception {

        Libro libro = new Libro();
        libro.setId(2);
        libro.setTitulo("El Principito");
        libro.setStock(2);
        libro.setDisponibilidad("disponible");

        when(service.buscarDisponibles()).thenReturn(List.of(libro));

        mockMvc.perform(get("/libros/disponibles"))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$[0].disponibilidad").value("disponible"));
    }
}