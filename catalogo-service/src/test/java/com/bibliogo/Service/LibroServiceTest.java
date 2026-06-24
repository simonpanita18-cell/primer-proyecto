package com.bibliogo.Service;

import com.bibliogo.catalogo_service.Exception.ConflictoException;
import com.bibliogo.catalogo_service.Exception.RecursoNoEncontradoException;
import com.bibliogo.catalogo_service.Model.Libro;
import com.bibliogo.catalogo_service.Repository.LibroRepository;
import com.bibliogo.catalogo_service.Service.LibroService;
import com.bibliogo.catalogo_service.dto.LibroRequestDTO;
import com.bibliogo.catalogo_service.dto.LibroResponseDTO;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LibroServiceTest {

    @Mock
    private LibroRepository repository;

    @InjectMocks
    private LibroService libroService;

    @Test
    void listar_debeRetornarListaDeLibros() {
        Libro libro = new Libro();
        libro.setId(1);
        libro.setTitulo("Cien años de soledad");
        libro.setAutor("Gabriel García Márquez");

        when(repository.findAll()).thenReturn(List.of(libro));

        List<Libro> resultado = libroService.listar();

        assertEquals(1, resultado.size());
        assertEquals("Cien años de soledad", resultado.get(0).getTitulo());
    }

    @Test
    void buscarPorId_cuandoExiste_debeRetornarLibro() {
        Libro libro = new Libro();
        libro.setId(1);
        libro.setTitulo("1984");

        when(repository.findById(1)).thenReturn(Optional.of(libro));

        Libro resultado = libroService.buscarPorId(1);

        assertEquals("1984", resultado.getTitulo());
    }

    @Test
    void buscarPorId_cuandoNoExiste_debeLanzarExcepcion() {
        when(repository.findById(99)).thenReturn(Optional.empty());

        assertThrows(RecursoNoEncontradoException.class,
                () -> libroService.buscarPorId(99));
    }

    @Test
    void buscarPorIsbn_cuandoNoExiste_debeLanzarExcepcion() {
        when(repository.findByIsbn("000-00-00")).thenReturn(Optional.empty());

        assertThrows(RecursoNoEncontradoException.class,
                () -> libroService.buscarPorIsbn("000-00-00"));
    }

    @Test
    void crear_conStockPositivo_debeQuedarDisponible() {
        LibroRequestDTO dto = new LibroRequestDTO();
        dto.setTitulo("El Principito");
        dto.setAutor("Antoine de Saint-Exupéry");
        dto.setCategoria("Novela");
        dto.setIsbn("978-00-00");
        dto.setStock(5);

        Libro guardado = new Libro();
        guardado.setId(1);
        guardado.setTitulo("El Principito");
        guardado.setStock(5);
        guardado.setDisponibilidad("disponible");

        when(repository.save(any(Libro.class))).thenReturn(guardado);

        LibroResponseDTO resultado = libroService.crear(dto);

        assertEquals("El Principito", resultado.getTitulo());
        assertEquals("disponible", resultado.getDisponibilidad());
    }

    @Test
    void crear_conStockCero_debeQuedarAgotado() {
        LibroRequestDTO dto = new LibroRequestDTO();
        dto.setTitulo("Libro sin stock");
        dto.setStock(0);

        Libro guardado = new Libro();
        guardado.setId(2);
        guardado.setTitulo("Libro sin stock");
        guardado.setStock(0);
        guardado.setDisponibilidad("agotado");

        when(repository.save(any(Libro.class))).thenReturn(guardado);

        LibroResponseDTO resultado = libroService.crear(dto);

        assertEquals("agotado", resultado.getDisponibilidad());
    }

    @Test
    void crear_conStockNegativo_debeLanzarConflicto() {
        LibroRequestDTO dto = new LibroRequestDTO();
        dto.setStock(-1);

        assertThrows(ConflictoException.class,
                () -> libroService.crear(dto));

        verify(repository, never()).save(any());
    }

    @Test
    void reducirStock_conStockDisponible_debeReducirCorrectamente() {
        Libro libro = new Libro();
        libro.setId(1);
        libro.setTitulo("1984");
        libro.setStock(3);
        libro.setDisponibilidad("disponible");

        when(repository.findById(1)).thenReturn(Optional.of(libro));
        when(repository.save(any(Libro.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Libro resultado = libroService.reducirStock(1);

        assertEquals(2, resultado.getStock());
        assertEquals("disponible", resultado.getDisponibilidad());
    }

    @Test
    void reducirStock_cuandoLlegaACero_debeQuedarNoDisponible() {
        Libro libro = new Libro();
        libro.setId(1);
        libro.setTitulo("1984");
        libro.setStock(1);
        libro.setDisponibilidad("disponible");

        when(repository.findById(1)).thenReturn(Optional.of(libro));
        when(repository.save(any(Libro.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Libro resultado = libroService.reducirStock(1);

        assertEquals(0, resultado.getStock());
        assertEquals("no disponible", resultado.getDisponibilidad());
    }

    @Test
    void reducirStock_sinStockDisponible_debeLanzarConflicto() {
        Libro libro = new Libro();
        libro.setId(1);
        libro.setTitulo("Sin Stock");
        libro.setStock(0);

        when(repository.findById(1)).thenReturn(Optional.of(libro));

        assertThrows(ConflictoException.class,
                () -> libroService.reducirStock(1));

        verify(repository, never()).save(any());
    }

    @Test
    void aumentarStock_debeAumentarYQuedarDisponible() {
        Libro libro = new Libro();
        libro.setId(1);
        libro.setTitulo("1984");
        libro.setStock(0);
        libro.setDisponibilidad("no disponible");

        when(repository.findById(1)).thenReturn(Optional.of(libro));
        when(repository.save(any(Libro.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Libro resultado = libroService.aumentarStock(1);

        assertEquals(1, resultado.getStock());
        assertEquals("disponible", resultado.getDisponibilidad());
    }

    @Test
    void eliminar_cuandoExiste_debeEliminarCorrectamente() {
        when(repository.existsById(1)).thenReturn(true);

        libroService.eliminar(1);

        verify(repository, times(1)).deleteById(1);
    }

    @Test
    void eliminar_cuandoNoExiste_debeLanzarExcepcion() {
        when(repository.existsById(99)).thenReturn(false);

        assertThrows(RecursoNoEncontradoException.class,
                () -> libroService.eliminar(99));

        verify(repository, never()).deleteById(any());
    }
}