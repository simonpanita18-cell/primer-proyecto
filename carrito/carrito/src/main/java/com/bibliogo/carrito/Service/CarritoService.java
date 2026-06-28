package com.bibliogo.carrito.Service;

import com.bibliogo.carrito.Model.Carrito;
import com.bibliogo.carrito.Repository.CarritoRepository;
import com.bibliogo.carrito.dto.CarritoCantidadDTO;
import com.bibliogo.carrito.dto.CarritoRequestDTO;
import com.bibliogo.carrito.dto.CarritoResponseDTO;
import com.bibliogo.carrito.exception.ConflictoException;
import com.bibliogo.carrito.exception.RecursoNoEncontradoException;
import com.bibliogo.carrito.exception.ServicioNoDisponibleException;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientRequestException;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.util.List;

@Slf4j
@Service
public class CarritoService {

    @Autowired
    private CarritoRepository repository;

    @Autowired
    private WebClient webClient;

    private void verificarLibroDisponible(Integer libroId) {
        log.info("Verificando disponibilidad del libro id: {}", libroId);

        try {
            String respuesta = webClient.get()
                    .uri("http://catalogo-service/libros/" + libroId)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();

            if (respuesta == null) {
                log.error("Sin respuesta de catalogo-service para libro id: {}", libroId);
                throw new ServicioNoDisponibleException(
                        "No se recibió respuesta de catalogo-service"
                );
            }

            if (respuesta.contains("no disponible")) {
                log.warn("Libro id: {} no disponible para agregar al carrito", libroId);
                throw new ConflictoException(
                        "El libro no está disponible para agregar al carrito"
                );
            }

        } catch (WebClientResponseException.NotFound e) {
            log.warn("Libro no encontrado con id: {}", libroId);
            throw new RecursoNoEncontradoException(
                    "Libro no encontrado con id: " + libroId
            );

        } catch (WebClientRequestException e) {
            log.error("catalogo-service no disponible al verificar libro id: {}", libroId);
            throw new ServicioNoDisponibleException(
                    "catalogo-service no está disponible. No se pudo verificar el libro con id: " + libroId
            );

        } catch (ConflictoException | RecursoNoEncontradoException | ServicioNoDisponibleException e) {
            throw e;

        } catch (Exception e) {
            log.error("Error inesperado al verificar libro id: {}", libroId, e);
            throw new ServicioNoDisponibleException(
                    "Error al comunicarse con catalogo-service para verificar el libro con id: " + libroId
            );
        }
    }

    public List<Carrito> listar() {
        log.info("Listando todos los items de carrito");
        return repository.findAll();
    }

    public Carrito buscarPorId(Integer id) {
        log.info("Buscando item de carrito con id: {}", id);
        return repository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Item de carrito no encontrado con id: {}", id);
                    return new RecursoNoEncontradoException("Item no encontrado con id: " + id);
                });
    }

    public List<Carrito> buscarPorUsuario(Integer usuarioId) {
        log.info("Buscando carrito del usuario id: {}", usuarioId);
        return repository.findByUsuarioId(usuarioId);
    }

    public List<Carrito> carritoActivoDeUsuario(Integer usuarioId) {
        log.info("Buscando carrito activo del usuario id: {}", usuarioId);
        return repository.findByUsuarioIdAndEstado(usuarioId, "activo");
    }

    public CarritoResponseDTO agregar(CarritoRequestDTO dto) {
        log.info("Agregando libro id: {} al carrito del usuario id: {}", dto.getLibroId(), dto.getUsuarioId());

        verificarLibroDisponible(dto.getLibroId());

        Carrito carrito = new Carrito();
        carrito.setUsuarioId(dto.getUsuarioId());
        carrito.setLibroId(dto.getLibroId());
        carrito.setTituloLibro(dto.getTituloLibro());
        carrito.setCantidad(dto.getCantidad());
        carrito.setEstado("activo");

        Carrito guardado = repository.save(carrito);

        log.info("Item de carrito creado con id: {}", guardado.getId());
        return convertirDTO(guardado);
    }

    public CarritoResponseDTO actualizarCantidad(Integer id, CarritoCantidadDTO dto) {
        log.info("Actualizando cantidad del item de carrito id: {}", id);

        Carrito carrito = buscarPorId(id);
        carrito.setCantidad(dto.getCantidad());

        Carrito actualizado = repository.save(carrito);

        log.info("Cantidad actualizada para item id: {} — nueva cantidad: {}", id, actualizado.getCantidad());
        return convertirDTO(actualizado);
    }

    public CarritoResponseDTO confirmar(Integer id) {
        log.info("Confirmando item de carrito id: {}", id);

        Carrito carrito = buscarPorId(id);
        carrito.setEstado("confirmado");

        Carrito actualizado = repository.save(carrito);

        log.info("Item de carrito id: {} confirmado correctamente", id);
        return convertirDTO(actualizado);
    }

    public void eliminar(Integer id) {
        log.info("Eliminando item de carrito con id: {}", id);

        if (!repository.existsById(id)) {
            log.warn("Item de carrito no encontrado con id: {}", id);
            throw new RecursoNoEncontradoException("Item no encontrado con id: " + id);
        }

        repository.deleteById(id);
        log.info("Item de carrito con id: {} eliminado correctamente", id);
    }

    public void vaciarCarrito(Integer usuarioId) {
        log.info("Vaciando carrito activo del usuario id: {}", usuarioId);

        List<Carrito> items = repository.findByUsuarioIdAndEstado(usuarioId, "activo");

        items.forEach(item -> item.setEstado("cancelado"));

        repository.saveAll(items);

        log.info("Carrito del usuario id: {} vaciado — {} items cancelados", usuarioId, items.size());
    }

    private CarritoResponseDTO convertirDTO(Carrito carrito) {
        return new CarritoResponseDTO(
                carrito.getId(),
                carrito.getUsuarioId(),
                carrito.getLibroId(),
                carrito.getTituloLibro(),
                carrito.getCantidad(),
                carrito.getEstado()
        );
    }
}