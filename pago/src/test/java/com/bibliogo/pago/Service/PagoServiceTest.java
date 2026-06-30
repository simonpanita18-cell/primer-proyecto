package com.bibliogo.pago.Service;

import com.bibliogo.pago.dto.PagoRequestDTO;
import com.bibliogo.pago.dto.PagoResponseDTO;
import com.bibliogo.pago.exception.ConflictoException;
import com.bibliogo.pago.exception.RecursoNoEncontradoException;
import com.bibliogo.pago.exception.ServicioNoDisponibleException;
import com.bibliogo.pago.model.Pago;
import com.bibliogo.pago.repository.PagoRepository;
import com.bibliogo.pago.service.PagoService;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientRequestException;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PagoServiceTest {

    @Mock
    private PagoRepository repository;

    @Mock
    private WebClient webClient;

    @Mock
    private WebClient.RequestHeadersUriSpec requestHeadersUriSpec;

    @Mock
    private WebClient.RequestHeadersSpec requestHeadersSpec;

    @Mock
    private WebClient.ResponseSpec responseSpec;

    @InjectMocks
    private PagoService pagoService;

    @SuppressWarnings("unchecked")
    private void mockGetExitoso(String respuesta) {
        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(anyString())).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(String.class)).thenReturn(Mono.just(respuesta));
    }

    @SuppressWarnings("unchecked")
    private void mockGetError(RuntimeException excepcion) {
        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(anyString())).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(String.class)).thenReturn(Mono.error(excepcion));
    }

    private RuntimeException errorDeConexion() {
        return new WebClientRequestException(
                new RuntimeException("Connection refused"),
                org.springframework.http.HttpMethod.GET,
                java.net.URI.create("http://localhost:8084/prestamos/1"),
                new org.springframework.http.HttpHeaders()
        );
    }

    @Test
    void listar_debeRetornarListaDePagos() {
        Pago pago = new Pago();
        pago.setId(1);
        pago.setTipo("multa");
        pago.setEstado("pendiente");

        when(repository.findAll()).thenReturn(List.of(pago));

        List<Pago> resultado = pagoService.listar();

        assertEquals(1, resultado.size());
        assertEquals("multa", resultado.get(0).getTipo());
    }

    @Test
    void buscarPorId_cuandoNoExiste_debeLanzarExcepcion() {
        when(repository.findById(99)).thenReturn(Optional.empty());

        assertThrows(RecursoNoEncontradoException.class,
                () -> pagoService.buscarPorId(99));
    }

    @Test
    void crear_conPrestamoValido_debeCrearPagoConEstadoPendiente() {
        PagoRequestDTO dto = new PagoRequestDTO();
        dto.setPrestamoId(1);
        dto.setUsuarioId(1);
        dto.setMonto(new BigDecimal("2500.00"));
        dto.setMetodo("tarjeta");
        dto.setTipo("multa");

        mockGetExitoso("{\"id\":1,\"estado\":\"devuelto con retraso\"}");

        Pago guardado = new Pago();
        guardado.setId(1);
        guardado.setPrestamoId(1);
        guardado.setUsuarioId(1);
        guardado.setMonto(new BigDecimal("2500.00"));
        guardado.setMetodo("tarjeta");
        guardado.setTipo("multa");
        guardado.setEstado("pendiente");
        guardado.setFechaPago(LocalDateTime.now());

        when(repository.save(any(Pago.class))).thenReturn(guardado);

        PagoResponseDTO resultado = pagoService.crear(dto);

        assertEquals("pendiente", resultado.getEstado());
        assertEquals("multa", resultado.getTipo());
    }

    @Test
    void crear_cuandoPrestamoServiceNoDisponible_debeLanzarServicioNoDisponible() {
        PagoRequestDTO dto = new PagoRequestDTO();
        dto.setPrestamoId(1);
        dto.setUsuarioId(1);
        dto.setMonto(new BigDecimal("2500.00"));

        mockGetError(errorDeConexion());

        assertThrows(ServicioNoDisponibleException.class,
                () -> pagoService.crear(dto));

        verify(repository, never()).save(any());
    }

    @Test
    void confirmar_conEstadoPendiente_debeCambiarAPagado() {
        Pago pago = new Pago();
        pago.setId(1);
        pago.setEstado("pendiente");

        when(repository.findById(1)).thenReturn(Optional.of(pago));
        when(repository.save(any(Pago.class))).thenAnswer(invocation -> invocation.getArgument(0));

        PagoResponseDTO resultado = pagoService.confirmar(1);

        assertEquals("pagado", resultado.getEstado());
    }

    @Test
    void confirmar_cuandoYaFueConfirmado_debeLanzarConflicto() {
        Pago pago = new Pago();
        pago.setId(1);
        pago.setEstado("pagado");

        when(repository.findById(1)).thenReturn(Optional.of(pago));

        assertThrows(ConflictoException.class,
                () -> pagoService.confirmar(1));

        verify(repository, never()).save(any());
    }

    @Test
    void confirmar_cuandoFueRechazado_debeLanzarConflicto() {
        Pago pago = new Pago();
        pago.setId(1);
        pago.setEstado("rechazado");

        when(repository.findById(1)).thenReturn(Optional.of(pago));

        assertThrows(ConflictoException.class,
                () -> pagoService.confirmar(1));
    }

    @Test
    void rechazar_conEstadoPendiente_debeCambiarARechazado() {
        Pago pago = new Pago();
        pago.setId(1);
        pago.setEstado("pendiente");

        when(repository.findById(1)).thenReturn(Optional.of(pago));
        when(repository.save(any(Pago.class))).thenAnswer(invocation -> invocation.getArgument(0));

        PagoResponseDTO resultado = pagoService.rechazar(1);

        assertEquals("rechazado", resultado.getEstado());
    }

    @Test
    void rechazar_cuandoYaFueConfirmado_debeLanzarConflicto() {
        Pago pago = new Pago();
        pago.setId(1);
        pago.setEstado("pagado");

        when(repository.findById(1)).thenReturn(Optional.of(pago));

        assertThrows(ConflictoException.class,
                () -> pagoService.rechazar(1));

        verify(repository, never()).save(any());
    }

    @Test
    void eliminar_cuandoExiste_debeEliminarCorrectamente() {
        when(repository.existsById(1)).thenReturn(true);

        pagoService.eliminar(1);

        verify(repository, times(1)).deleteById(1);
    }

    @Test
    void eliminar_cuandoNoExiste_debeLanzarExcepcion() {
        when(repository.existsById(99)).thenReturn(false);

        assertThrows(RecursoNoEncontradoException.class,
                () -> pagoService.eliminar(99));
    }
}