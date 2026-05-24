package com.bibliogo.carrito.Service;

import com.bibliogo.carrito.Model.Carrito;
import com.bibliogo.carrito.Repository.CarritoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import java.util.List;
import java.util.Optional;

@Service
public class CarritoService {

    @Autowired
    private CarritoRepository repository;

    @Autowired
    private WebClient webClient;

    // Verifica que el libro existe y tiene stock en catalogo-service
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
            if (e.getMessage() != null && e.getMessage().contains("no está disponible")) {
                throw e;
            }
            // Si catalogo-service no responde, igual permite agregar al carrito
        }
    }

    public List<Carrito> listar() {
        return repository.findAll();
    }

    public Optional<Carrito> buscarPorId(Integer id) {
        return repository.findById(id);
    }

    public List<Carrito> buscarPorUsuario(Integer usuarioId) {
        return repository.findByUsuarioId(usuarioId);
    }

    public List<Carrito> carritoActivoDeUsuario(Integer usuarioId) {
        return repository.findByUsuarioIdAndEstado(usuarioId, "activo");
    }

    public Carrito agregar(Carrito carrito) {
        // Regla de negocio: verificar que el libro existe y tiene stock
        verificarLibroDisponible(carrito.getLibroId());
        carrito.setEstado("activo");
        return repository.save(carrito);
    }

    public Carrito actualizarCantidad(Integer id, Integer cantidad) {
        Carrito carrito = repository.findById(id)
            .orElseThrow(() -> new RuntimeException("Item no encontrado con id: " + id));
        carrito.setCantidad(cantidad);
        return repository.save(carrito);
    }

    public Carrito confirmar(Integer id) {
        Carrito carrito = repository.findById(id)
            .orElseThrow(() -> new RuntimeException("Item no encontrado con id: " + id));
        carrito.setEstado("confirmado");
        return repository.save(carrito);
    }

    public void eliminar(Integer id) {
        repository.deleteById(id);
    }

    public void vaciarCarrito(Integer usuarioId) {
        List<Carrito> items = repository.findByUsuarioIdAndEstado(usuarioId, "activo");
        items.forEach(item -> item.setEstado("cancelado"));
        repository.saveAll(items);
    }
}