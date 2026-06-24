package com.bibliogo.prestamo.Service;

import com.bibliogo.prestamo.dto.PrestamoRequestDTO;
import com.bibliogo.prestamo.dto.PrestamoResponseDTO;
import com.bibliogo.prestamo.dto.PrestamoUpdateDTO;
import com.bibliogo.prestamo.exeption.ConflictoException;
import com.bibliogo.prestamo.exeption.RecursoNoEncontradoException;
import com.bibliogo.prestamo.exeption.ServicioNoDisponibleException;
import com.bibliogo.prestamo.model.Prestamo;
import com.bibliogo.prestamo.repository.PrestamoRepository;
import com.bibliogo.prestamo.service.PrestamoService;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientRequestException;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PrestamoServiceTest {

    @Mock
    private PrestamoRepository repository;

    @Mock
    private WebClient webClient;

    @Mock
    private WebClient.RequestHeadersUriSpec requestHeadersUriSpecGet;

    @Mock
    private WebClient.RequestBodyUriSpec requestBodyUriSpecPut;

    @Mock
    private WebClient.RequestBodySpec requestBodySpec;

    @Mock
    private WebClient.RequestHeadersSpec requestHeadersSpec;

    @Mock
    private WebClient.ResponseSpec responseSpec;

    @InjectMocks
    private PrestamoService prestamoService;

    // ── Helpers para mockear las cadenas fluidas de WebClient ──────

    @SuppressWarnings("unchecked")
    private void mockGetExitoso(String respuesta) {
        when(webClient.get()).thenReturn(requestHeadersUriSpecGet);
        when(requestHeadersUriSpecGet.uri(anyString())).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(String.class)).thenReturn(Mono.just(respuesta));
    }

    @SuppressWarnings("unchecked")
    private void mockGetError(RuntimeException excepcion) {
        when(webClient.get()).thenReturn(requestHeadersUriSpecGet);
        when(requestHeadersUriSpecGet.uri(anyString())).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(String.class)).thenReturn(Mono.error(excepcion));
    }

    @SuppressWarnings("unchecked")
    private void mockPutExitoso(String respuesta) {
        when(webClient.put()).thenReturn(requestBodyUriSpecPut);
        when(requestBodyUriSpecPut.uri(anyString())).thenReturn(requestBodySpec);
        when(requestBodySpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(String.class)).thenReturn(Mono.just(respuesta));
    }

    private RuntimeException errorDeConexion() {
        return new WebClientRequestException(
                new RuntimeException("Connection refused"),
                org.springframework.http.HttpMethod.GET,
                java.net.URI.create("http://localhost:8081/usuarios/1"),
                new org.springframework.http.HttpHeaders()
        );
    }

    // ── TESTS DE LISTADO Y BÚSQUEDA ──────────────────────────────

    @Test
    void listar_debeRetornarListaDePrestamos() {
        Prestamo prestamo = new Prestamo();
        prestamo.setId(1);
        prestamo.setTituloLibro("Cien años de soledad");
        prestamo.setEstado("activo");

        when(repository.findAll()).thenReturn(List.of(prestamo));

        List<PrestamoResponseDTO> resultado = prestamoService.listar();

        assertEquals(1, resultado.size());
        assertEquals("Cien años de soledad", resultado.get(0).getTituloLibro());
    }

    @Test
    void buscarPorId_cuandoNoExiste_debeLanzarExcepcion() {
        when(repository.findById(99)).thenReturn(Optional.empty());

        assertThrows(RecursoNoEncontradoException.class,
                () -> prestamoService.buscarPorId(99));
    }

    // ── TESTS DE CREAR (la lógica más importante) ────────────────

    @Test
    void crear_conUsuarioYLibroValidos_debeCrearPrestamoYReducirStock() {
        PrestamoRequestDTO dto = new PrestamoRequestDTO();
        dto.setUsuarioId(1);
        dto.setLibroId(2);
        dto.setTituloLibro("1984");

        // Simula: verificarUsuario() OK -> verificarLibro() OK -> reducirStockLibro() OK
        mockGetExitoso("{\"id\":1,\"nombre\":\"Simon\"}");

        Prestamo guardado = new Prestamo();
        guardado.setId(1);
        guardado.setUsuarioId(1);
        guardado.setLibroId(2);
        guardado.setTituloLibro("1984");
        guardado.setFechaPrestamo(LocalDate.now());
        guardado.setFechaDevolucion(LocalDate.now().plusDays(7));
        guardado.setEstado("activo");

        when(repository.save(any(Prestamo.class))).thenReturn(guardado);

        mockPutExitoso("Stock reducido correctamente");

        PrestamoResponseDTO resultado = prestamoService.crear(dto);

        assertEquals("1984", resultado.getTituloLibro());
        assertEquals("activo", resultado.getEstado());
        verify(repository, times(1)).save(any(Prestamo.class));
    }

    @Test
    void crear_cuandoUsuariosMicroNoDisponible_debeLanzarServicioNoDisponible() {
        PrestamoRequestDTO dto = new PrestamoRequestDTO();
        dto.setUsuarioId(1);
        dto.setLibroId(2);

        mockGetError(errorDeConexion());

        assertThrows(ServicioNoDisponibleException.class,
                () -> prestamoService.crear(dto));

        verify(repository, never()).save(any());
    }

    // ── TESTS DE DEVOLVER ────────────────────────────────────────

    @Test
    void devolver_aTiempo_debeQuedarEstadoDevuelto() {
        Prestamo prestamo = new Prestamo();
        prestamo.setId(1);
        prestamo.setLibroId(2);
        prestamo.setEstado("activo");
        prestamo.setFechaDevolucion(LocalDate.now().plusDays(3)); // aún no vence

        when(repository.findById(1)).thenReturn(Optional.of(prestamo));
        when(repository.save(any(Prestamo.class))).thenAnswer(invocation -> invocation.getArgument(0));

        mockPutExitoso("Stock aumentado correctamente");

        PrestamoResponseDTO resultado = prestamoService.devolver(1);

        assertEquals("devuelto", resultado.getEstado());
    }

    @Test
    void devolver_conRetraso_debeQuedarEstadoDevueltoConRetraso() {
        Prestamo prestamo = new Prestamo();
        prestamo.setId(1);
        prestamo.setLibroId(2);
        prestamo.setEstado("activo");
        prestamo.setFechaDevolucion(LocalDate.now().minusDays(2)); // ya venció

        when(repository.findById(1)).thenReturn(Optional.of(prestamo));
        when(repository.save(any(Prestamo.class))).thenAnswer(invocation -> invocation.getArgument(0));

        mockPutExitoso("Stock aumentado correctamente");

        PrestamoResponseDTO resultado = prestamoService.devolver(1);

        assertEquals("devuelto con retraso", resultado.getEstado());
    }

    @Test
    void devolver_cuandoYaFueDevuelto_debeLanzarConflicto() {
        Prestamo prestamo = new Prestamo();
        prestamo.setId(1);
        prestamo.setEstado("devuelto");

        when(repository.findById(1)).thenReturn(Optional.of(prestamo));

        assertThrows(ConflictoException.class,
                () -> prestamoService.devolver(1));

        verify(repository, never()).save(any());
    }

    @Test
    void devolver_cuandoPrestamoNoExiste_debeLanzarExcepcion() {
        when(repository.findById(99)).thenReturn(Optional.empty());

        assertThrows(RecursoNoEncontradoException.class,
                () -> prestamoService.devolver(99));
    }

    // ── TESTS DE ACTUALIZAR Y ELIMINAR ───────────────────────────

    @Test
    void actualizar_debeActualizarEstadoYObservaciones() {
        Prestamo prestamo = new Prestamo();
        prestamo.setId(1);
        prestamo.setEstado("activo");

        PrestamoUpdateDTO dto = new PrestamoUpdateDTO();
        dto.setEstado("devuelto");
        dto.setObservaciones("Devuelto en buen estado");

        when(repository.findById(1)).thenReturn(Optional.of(prestamo));
        when(repository.save(any(Prestamo.class))).thenAnswer(invocation -> invocation.getArgument(0));

        PrestamoResponseDTO resultado = prestamoService.actualizar(1, dto);

        assertEquals("devuelto", resultado.getEstado());
    }

    @Test
    void eliminar_cuandoExiste_debeEliminarCorrectamente() {
        when(repository.existsById(1)).thenReturn(true);

        prestamoService.eliminar(1);

        verify(repository, times(1)).deleteById(1);
    }

    @Test
    void eliminar_cuandoNoExiste_debeLanzarExcepcion() {
        when(repository.existsById(99)).thenReturn(false);

        assertThrows(RecursoNoEncontradoException.class,
                () -> prestamoService.eliminar(99));
    }
}