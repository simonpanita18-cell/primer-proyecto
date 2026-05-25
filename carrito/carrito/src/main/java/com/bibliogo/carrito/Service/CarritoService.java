package com.bibliogo.carrito.Service;

import com.bibliogo.carrito.Model.Carrito;
import com.bibliogo.carrito.Repository.CarritoRepository;
import com.bibliogo.carrito.dto.CarritoCantidadDTO;
import com.bibliogo.carrito.dto.CarritoRequestDTO;
import com.bibliogo.carrito.dto.CarritoResponseDTO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

@Service
public class CarritoService {

    @Autowired
    private CarritoRepository repository;

    @Autowired
    private WebClient webClient;

    private void verificarLibroDisponible(Integer libroId) {

        try {

            String respuesta = webClient.get()
                    .uri("http://localhost:8082/libros/" + libroId)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();

            if (respuesta == null || respuesta.contains("no disponible")) {
                throw new RuntimeException("El libro no está disponible para reservar");
            }

        } catch (Exception e) {

            if (e.getMessage() != null &&
                    e.getMessage().contains("no está disponible")) {

                throw e;
            }
        }
    }

    public List<Carrito> listar() {
        return repository.findAll();
    }

    public Carrito buscarPorId(Integer id) {
        return repository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Item no encontrado con id: " + id));
    }

    public List<Carrito> buscarPorUsuario(Integer usuarioId) {
        return repository.findByUsuarioId(usuarioId);
    }

    public List<Carrito> carritoActivoDeUsuario(Integer usuarioId) {
        return repository.findByUsuarioIdAndEstado(usuarioId, "activo");
    }

    public CarritoResponseDTO agregar(CarritoRequestDTO dto) {

        verificarLibroDisponible(dto.getLibroId());

        Carrito carrito = new Carrito();

        carrito.setUsuarioId(dto.getUsuarioId());
        carrito.setLibroId(dto.getLibroId());
        carrito.setTituloLibro(dto.getTituloLibro());
        carrito.setCantidad(dto.getCantidad());
        carrito.setEstado("activo");

        Carrito guardado = repository.save(carrito);

        return convertirDTO(guardado);
    }

    public CarritoResponseDTO actualizarCantidad(Integer id, CarritoCantidadDTO dto) {

        Carrito carrito = buscarPorId(id);

        carrito.setCantidad(dto.getCantidad());

        Carrito actualizado = repository.save(carrito);

        return convertirDTO(actualizado);
    }

    public CarritoResponseDTO confirmar(Integer id) {

        Carrito carrito = buscarPorId(id);

        carrito.setEstado("confirmado");

        Carrito actualizado = repository.save(carrito);

        return convertirDTO(actualizado);
    }

    public void eliminar(Integer id) {

        if (!repository.existsById(id)) {
            throw new RuntimeException("Item no encontrado con id: " + id);
        }

        repository.deleteById(id);
    }

    public void vaciarCarrito(Integer usuarioId) {

        List<Carrito> items =
                repository.findByUsuarioIdAndEstado(usuarioId, "activo");

        items.forEach(item -> item.setEstado("cancelado"));

        repository.saveAll(items);
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