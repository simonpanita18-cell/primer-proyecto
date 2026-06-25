package com.bibliogo.Service;

import com.bibliogo.reporte.dto.ReporteRequestDTO;
import com.bibliogo.reporte.dto.ReporteResponseDTO;
import com.bibliogo.reporte.exception.RecursoNoEncontradoException;
import com.bibliogo.reporte.model.Reporte;
import com.bibliogo.reporte.repository.ReporteRepository;
import com.bibliogo.reporte.service.ReporteService;

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
class ReporteServiceTest {

    @Mock
    private ReporteRepository repository;

    @InjectMocks
    private ReporteService reporteService;

    @Test
    void listar_debeRetornarListaDeReportes() {
        Reporte reporte = new Reporte();
        reporte.setId(1);
        reporte.setTipo("prestamos");
        reporte.setGeneradoPor("admin");

        when(repository.findAll()).thenReturn(List.of(reporte));

        List<Reporte> resultado = reporteService.listar();

        assertEquals(1, resultado.size());
        assertEquals("prestamos", resultado.get(0).getTipo());
    }

    @Test
    void buscarPorId_cuandoExiste_debeRetornarReporte() {
        Reporte reporte = new Reporte();
        reporte.setId(1);
        reporte.setTipo("usuarios");

        when(repository.findById(1)).thenReturn(Optional.of(reporte));

        Reporte resultado = reporteService.buscarPorId(1);

        assertEquals("usuarios", resultado.getTipo());
    }

    @Test
    void buscarPorId_cuandoNoExiste_debeLanzarExcepcion() {
        when(repository.findById(99)).thenReturn(Optional.empty());

        assertThrows(RecursoNoEncontradoException.class,
                () -> reporteService.buscarPorId(99));
    }

    @Test
    void buscarPorTipo_debeRetornarReportesDeEseTipo() {
        Reporte reporte = new Reporte();
        reporte.setId(1);
        reporte.setTipo("prestamos");

        when(repository.findByTipo("prestamos")).thenReturn(List.of(reporte));

        List<Reporte> resultado = reporteService.buscarPorTipo("prestamos");

        assertEquals(1, resultado.size());
        assertEquals("prestamos", resultado.get(0).getTipo());
    }

    @Test
    void buscarPorGeneradoPor_debeRetornarReportesDeEsePerfil() {
        Reporte reporte = new Reporte();
        reporte.setId(1);
        reporte.setGeneradoPor("admin");

        when(repository.findByGeneradoPor("admin")).thenReturn(List.of(reporte));

        List<Reporte> resultado = reporteService.buscarPorGeneradoPor("admin");

        assertEquals(1, resultado.size());
        assertEquals("admin", resultado.get(0).getGeneradoPor());
    }

    @Test
    void crear_debeCrearReporteCorrectamente() {
        ReporteRequestDTO dto = new ReporteRequestDTO();
        dto.setTipo("prestamos");
        dto.setDatos("Reporte de préstamos activos");
        dto.setGeneradoPor("admin");

        Reporte guardado = new Reporte();
        guardado.setId(1);
        guardado.setTipo("prestamos");
        guardado.setDatos("Reporte de préstamos activos");
        guardado.setGeneradoPor("admin");

        when(repository.save(any(Reporte.class))).thenReturn(guardado);

        ReporteResponseDTO resultado = reporteService.crear(dto);

        assertEquals("prestamos", resultado.getTipo());
        assertEquals("admin", resultado.getGeneradoPor());
    }

    @Test
    void eliminar_cuandoExiste_debeEliminarCorrectamente() {
        when(repository.existsById(1)).thenReturn(true);

        reporteService.eliminar(1);

        verify(repository, times(1)).deleteById(1);
    }

    @Test
    void eliminar_cuandoNoExiste_debeLanzarExcepcion() {
        when(repository.existsById(99)).thenReturn(false);

        assertThrows(RecursoNoEncontradoException.class,
                () -> reporteService.eliminar(99));
    }
}