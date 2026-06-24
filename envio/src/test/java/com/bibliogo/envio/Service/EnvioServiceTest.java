package com.bibliogo.envio.Service;

import com.bibliogo.envio.dto.EnvioRequestDTO;
import com.bibliogo.envio.dto.EnvioResponseDTO;
import com.bibliogo.envio.execption.ConflictoException;
import com.bibliogo.envio.execption.RecursoNoEncontradoException;
import com.bibliogo.envio.execption.ServicioNoDisponibleException;
import com.bibliogo.envio.model.Envio;
import com.bibliogo.envio.repository.EnvioRepository;
import com.bibliogo.envio.service.EnvioService;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientRequestException;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EnvioServiceTest {

    @Mock
    private EnvioRepository repository;

    @Mock
    private WebClient webClient;

    @Mock
    private WebClient.RequestHeadersUriSpec requestHeadersUriSpec;

    @Mock
    private WebClient.RequestHeadersSpec requestHeadersSpec;

    @Mock
    private WebClient.ResponseSpec responseSpec;

    @InjectMocks
    private EnvioService envioService;

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
    void listar_debeRetornarListaDeEnvios() {
        Envio envio = new Envio();
        envio.setId(1);
        envio.setComuna("Quilicura");
        envio.setEstado("entregado");

        when(repository.findAll()).thenReturn(List.of(envio));

        List<Envio> resultado = envioService.listar();

        assertEquals(1, resultado.size());
        assertEquals("Quilicura", resultado.get(0).getComuna());
    }

    @Test
    void buscarPorId_cuandoNoExiste_debeLanzarExcepcion() {
        when(repository.findById(99)).thenReturn(Optional.empty());

        assertThrows(RecursoNoEncontradoException.class,
                () -> envioService.buscarPorId(99));
    }

    @Test
    void crear_conPrestamoValido_debeCrearEnvioConEstadoPendiente() {
        EnvioRequestDTO dto = new EnvioRequestDTO();
        dto.setPrestamoId(1);
        dto.setUsuarioId(1);
        dto.setDireccion("Av. Principal 123");
        dto.setComuna("Quilicura");

        mockGetExitoso("{\"id\":1,\"estado\":\"activo\"}");

        Envio guardado = new Envio();
        guardado.setId(1);
        guardado.setPrestamoId(1);
        guardado.setUsuarioId(1);
        guardado.setDireccion("Av. Principal 123");
        guardado.setComuna("Quilicura");
        guardado.setEstado("pendiente");
        guardado.setFechaCreacion(LocalDateTime.now());

        when(repository.save(any(Envio.class))).thenReturn(guardado);

        EnvioResponseDTO resultado = envioService.crear(dto);

        assertEquals("pendiente", resultado.getEstado());
        assertEquals("Quilicura", resultado.getComuna());
    }

    @Test
    void crear_cuandoPrestamoServiceNoDisponible_debeLanzarServicioNoDisponible() {
        EnvioRequestDTO dto = new EnvioRequestDTO();
        dto.setPrestamoId(1);
        dto.setUsuarioId(1);

        mockGetError(errorDeConexion());

        assertThrows(ServicioNoDisponibleException.class,
                () -> envioService.crear(dto));

        verify(repository, never()).save(any());
    }

    @Test
    void despachar_conEstadoPendiente_debeCambiarAEnCamino() {
        Envio envio = new Envio();
        envio.setId(1);
        envio.setEstado("pendiente");

        when(repository.findById(1)).thenReturn(Optional.of(envio));
        when(repository.save(any(Envio.class))).thenAnswer(invocation -> invocation.getArgument(0));

        EnvioResponseDTO resultado = envioService.despachar(1);

        assertEquals("en camino", resultado.getEstado());
    }

    @Test
    void despachar_cuandoYaFueEntregado_debeLanzarConflicto() {
        Envio envio = new Envio();
        envio.setId(1);
        envio.setEstado("entregado");

        when(repository.findById(1)).thenReturn(Optional.of(envio));

        assertThrows(ConflictoException.class,
                () -> envioService.despachar(1));

        verify(repository, never()).save(any());
    }

    @Test
    void despachar_cuandoYaEstaEnCamino_debeLanzarConflicto() {
        Envio envio = new Envio();
        envio.setId(1);
        envio.setEstado("en camino");

        when(repository.findById(1)).thenReturn(Optional.of(envio));

        assertThrows(ConflictoException.class,
                () -> envioService.despachar(1));
    }

    @Test
    void entregar_debeCambiarEstadoYRegistrarFecha() {
        Envio envio = new Envio();
        envio.setId(1);
        envio.setEstado("en camino");

        when(repository.findById(1)).thenReturn(Optional.of(envio));
        when(repository.save(any(Envio.class))).thenAnswer(invocation -> invocation.getArgument(0));

        EnvioResponseDTO resultado = envioService.entregar(1);

        assertEquals("entregado", resultado.getEstado());
    }

    @Test
    void entregar_cuandoYaFueEntregado_debeLanzarConflicto() {
        Envio envio = new Envio();
        envio.setId(1);
        envio.setEstado("entregado");

        when(repository.findById(1)).thenReturn(Optional.of(envio));

        assertThrows(ConflictoException.class,
                () -> envioService.entregar(1));

        verify(repository, never()).save(any());
    }

    @Test
    void eliminar_cuandoExiste_debeEliminarCorrectamente() {
        when(repository.existsById(1)).thenReturn(true);

        envioService.eliminar(1);

        verify(repository, times(1)).deleteById(1);
    }

    @Test
    void eliminar_cuandoNoExiste_debeLanzarExcepcion() {
        when(repository.existsById(99)).thenReturn(false);

        assertThrows(RecursoNoEncontradoException.class,
                () -> envioService.eliminar(99));
    }
}