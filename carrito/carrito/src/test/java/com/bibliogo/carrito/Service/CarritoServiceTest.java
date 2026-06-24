package com.bibliogo.carrito.Service;

import com.bibliogo.carrito.Model.Carrito;
import com.bibliogo.carrito.Repository.CarritoRepository;
import com.bibliogo.carrito.dto.CarritoCantidadDTO;
import com.bibliogo.carrito.dto.CarritoRequestDTO;
import com.bibliogo.carrito.dto.CarritoResponseDTO;
import com.bibliogo.carrito.exception.RecursoNoEncontradoException;
import com.bibliogo.carrito.exception.ServicioNoDisponibleException;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientRequestException;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CarritoServiceTest {

    @Mock
    private CarritoRepository repository;

    @Mock
    private WebClient webClient;

    @Mock
    private WebClient.RequestHeadersUriSpec requestHeadersUriSpec;

    @Mock
    private WebClient.RequestHeadersSpec requestHeadersSpec;

    @Mock
    private WebClient.ResponseSpec responseSpec;

    @InjectMocks
    private CarritoService carritoService;

    // Helper para mockear la cadena fluida de WebClient.get()...
    @SuppressWarnings("unchecked")
    private void mockWebClientGetRespuesta(String respuesta) {
        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(anyString())).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(String.class)).thenReturn(reactor.core.publisher.Mono.just(respuesta));
    }

    @SuppressWarnings("unchecked")
    private void mockWebClientGetError(RuntimeException excepcion) {
        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(anyString())).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(String.class)).thenReturn(reactor.core.publisher.Mono.error(excepcion));
    }

    @Test
    void listar_debeRetornarListaDeItems() {
        Carrito carrito = new Carrito();
        carrito.setId(1);
        carrito.setTituloLibro("Cien años de soledad");
        carrito.setEstado("activo");

        when(repository.findAll()).thenReturn(List.of(carrito));

        List<Carrito> resultado = carritoService.listar();

        assertEquals(1, resultado.size());
        assertEquals("Cien años de soledad", resultado.get(0).getTituloLibro());
    }

    @Test
    void buscarPorId_cuandoExiste_debeRetornarItem() {
        Carrito carrito = new Carrito();
        carrito.setId(1);
        carrito.setTituloLibro("1984");

        when(repository.findById(1)).thenReturn(Optional.of(carrito));

        Carrito resultado = carritoService.buscarPorId(1);

        assertEquals("1984", resultado.getTituloLibro());
    }

    @Test
    void buscarPorId_cuandoNoExiste_debeLanzarExcepcion() {
        when(repository.findById(99)).thenReturn(Optional.empty());

        assertThrows(RecursoNoEncontradoException.class,
                () -> carritoService.buscarPorId(99));
    }

    @Test
    void agregar_conLibroDisponible_debeAgregarCorrectamente() {
        CarritoRequestDTO dto = new CarritoRequestDTO();
        dto.setUsuarioId(1);
        dto.setLibroId(2);
        dto.setTituloLibro("Cien años de soledad");
        dto.setCantidad(1);

        mockWebClientGetRespuesta("{\"disponibilidad\":\"disponible\"}");

        Carrito guardado = new Carrito();
        guardado.setId(1);
        guardado.setUsuarioId(1);
        guardado.setLibroId(2);
        guardado.setTituloLibro("Cien años de soledad");
        guardado.setCantidad(1);
        guardado.setEstado("activo");

        when(repository.save(any(Carrito.class))).thenReturn(guardado);

        CarritoResponseDTO resultado = carritoService.agregar(dto);

        assertEquals("Cien años de soledad", resultado.getTituloLibro());
        assertEquals("activo", resultado.getEstado());
    }

    @Test
    void agregar_cuandoCatalogoServiceNoDisponible_debeLanzarServicioNoDisponible() {
        CarritoRequestDTO dto = new CarritoRequestDTO();
        dto.setUsuarioId(1);
        dto.setLibroId(2);

        mockWebClientGetError(new WebClientRequestException(
                new RuntimeException("Connection refused"),
                org.springframework.http.HttpMethod.GET,
                java.net.URI.create("http://localhost:8082/libros/2"),
                new org.springframework.http.HttpHeaders()
        ));

        assertThrows(ServicioNoDisponibleException.class,
                () -> carritoService.agregar(dto));

        verify(repository, never()).save(any());
    }

    @Test
    void actualizarCantidad_debeActualizarCorrectamente() {
        Carrito carrito = new Carrito();
        carrito.setId(1);
        carrito.setCantidad(1);

        CarritoCantidadDTO dto = new CarritoCantidadDTO();
        dto.setCantidad(3);

        when(repository.findById(1)).thenReturn(Optional.of(carrito));
        when(repository.save(any(Carrito.class))).thenAnswer(invocation -> invocation.getArgument(0));

        CarritoResponseDTO resultado = carritoService.actualizarCantidad(1, dto);

        assertEquals(3, resultado.getCantidad());
    }

    @Test
    void confirmar_debeCambiarEstadoAConfirmado() {
        Carrito carrito = new Carrito();
        carrito.setId(1);
        carrito.setEstado("activo");

        when(repository.findById(1)).thenReturn(Optional.of(carrito));
        when(repository.save(any(Carrito.class))).thenAnswer(invocation -> invocation.getArgument(0));

        CarritoResponseDTO resultado = carritoService.confirmar(1);

        assertEquals("confirmado", resultado.getEstado());
    }

    @Test
    void eliminar_cuandoExiste_debeEliminarCorrectamente() {
        when(repository.existsById(1)).thenReturn(true);

        carritoService.eliminar(1);

        verify(repository, times(1)).deleteById(1);
    }

    @Test
    void eliminar_cuandoNoExiste_debeLanzarExcepcion() {
        when(repository.existsById(99)).thenReturn(false);

        assertThrows(RecursoNoEncontradoException.class,
                () -> carritoService.eliminar(99));
    }

    @Test
    void vaciarCarrito_debeCancelarTodosLosItemsActivos() {
        Carrito item1 = new Carrito();
        item1.setId(1);
        item1.setEstado("activo");

        Carrito item2 = new Carrito();
        item2.setId(2);
        item2.setEstado("activo");

        when(repository.findByUsuarioIdAndEstado(1, "activo"))
                .thenReturn(List.of(item1, item2));

        carritoService.vaciarCarrito(1);

        assertEquals("cancelado", item1.getEstado());
        assertEquals("cancelado", item2.getEstado());
        verify(repository, times(1)).saveAll(List.of(item1, item2));
    }
}