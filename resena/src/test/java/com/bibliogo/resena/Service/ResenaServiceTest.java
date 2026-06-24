package com.bibliogo.resena.Service;

import com.bibliogo.resena.dto.ResenaRequestDTO;
import com.bibliogo.resena.dto.ResenaResponseDTO;
import com.bibliogo.resena.dto.ResenaUpdateDTO;
import com.bibliogo.resena.exception.RecursoNoEncontradoException;
import com.bibliogo.resena.model.Resena;
import com.bibliogo.resena.repository.ResenaRepository;
import com.bibliogo.resena.service.ResenaService;

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
class ResenaServiceTest {

    @Mock
    private ResenaRepository repository;

    @InjectMocks
    private ResenaService resenaService;

    @Test
    void listar_debeRetornarListaDeResenas() {
        Resena resena = new Resena();
        resena.setId(1);
        resena.setCalificacion(5);
        resena.setComentario("Muy bueno");

        when(repository.findAll()).thenReturn(List.of(resena));

        List<Resena> resultado = resenaService.listar();

        assertEquals(1, resultado.size());
        assertEquals("Muy bueno", resultado.get(0).getComentario());
    }

    @Test
    void buscarPorId_cuandoExiste_debeRetornarResena() {
        Resena resena = new Resena();
        resena.setId(1);
        resena.setComentario("Buena lectura");

        when(repository.findById(1)).thenReturn(Optional.of(resena));

        Resena resultado = resenaService.buscarPorId(1);

        assertEquals("Buena lectura", resultado.getComentario());
    }

    @Test
    void buscarPorId_cuandoNoExiste_debeLanzarExcepcion() {
        when(repository.findById(99)).thenReturn(Optional.empty());

        assertThrows(RecursoNoEncontradoException.class,
                () -> resenaService.buscarPorId(99));
    }

    @Test
    void buscarPorLibro_debeRetornarResenasDelLibro() {
        Resena resena = new Resena();
        resena.setId(1);
        resena.setLibroId(1);
        resena.setCalificacion(5);

        when(repository.findByLibroId(1)).thenReturn(List.of(resena));

        List<Resena> resultado = resenaService.buscarPorLibro(1);

        assertEquals(1, resultado.size());
        assertEquals(5, resultado.get(0).getCalificacion());
    }

    @Test
    void buscarPorCalificacion_debeRetornarResenasConEsaCalificacion() {
        Resena resena = new Resena();
        resena.setId(1);
        resena.setCalificacion(5);

        when(repository.findByCalificacion(5)).thenReturn(List.of(resena));

        List<Resena> resultado = resenaService.buscarPorCalificacion(5);

        assertEquals(1, resultado.size());
    }

    @Test
    void crear_debeCrearResenaCorrectamente() {
        ResenaRequestDTO dto = new ResenaRequestDTO();
        dto.setUsuarioId(1);
        dto.setLibroId(1);
        dto.setCalificacion(5);
        dto.setComentario("Excelente libro");

        Resena guardada = new Resena();
        guardada.setId(1);
        guardada.setUsuarioId(1);
        guardada.setLibroId(1);
        guardada.setCalificacion(5);
        guardada.setComentario("Excelente libro");

        when(repository.save(any(Resena.class))).thenReturn(guardada);

        ResenaResponseDTO resultado = resenaService.crear(dto);

        assertEquals("Excelente libro", resultado.getComentario());
        assertEquals(5, resultado.getCalificacion());
    }

    @Test
    void actualizar_debeActualizarCalificacionYComentario() {
        Resena resena = new Resena();
        resena.setId(1);
        resena.setCalificacion(3);
        resena.setComentario("Regular");

        ResenaUpdateDTO dto = new ResenaUpdateDTO();
        dto.setCalificacion(5);
        dto.setComentario("Mejoró en la segunda lectura");

        when(repository.findById(1)).thenReturn(Optional.of(resena));
        when(repository.save(any(Resena.class))).thenAnswer(invocation -> invocation.getArgument(0));

        ResenaResponseDTO resultado = resenaService.actualizar(1, dto);

        assertEquals(5, resultado.getCalificacion());
        assertEquals("Mejoró en la segunda lectura", resultado.getComentario());
    }

    @Test
    void eliminar_cuandoExiste_debeEliminarCorrectamente() {
        when(repository.existsById(1)).thenReturn(true);

        resenaService.eliminar(1);

        verify(repository, times(1)).deleteById(1);
    }

    @Test
    void eliminar_cuandoNoExiste_debeLanzarExcepcion() {
        when(repository.existsById(99)).thenReturn(false);

        assertThrows(RecursoNoEncontradoException.class,
                () -> resenaService.eliminar(99));
    }
}