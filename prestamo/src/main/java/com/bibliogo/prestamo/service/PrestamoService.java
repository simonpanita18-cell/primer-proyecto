package com.bibliogo.prestamo.service;

import com.bibliogo.prestamo.model.Prestamo;
import com.bibliogo.prestamo.repository.PrestamoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class PrestamoService {

    @Autowired
    private PrestamoRepository repository;

    @Autowired
    private WebClient webClient;

    // Verifica si el libro existe y tiene stock en catalogo-service
    private void verificarLibro(Integer libroId) {
        try {
            String disponibilidad = webClient.get()
                .uri("http://localhost:8082/libros/" + libroId)
                .retrieve()
                .bodyToMono(String.class)
                .block();

            if (disponibilidad == null || disponibilidad.contains("no disponible")) {
                throw new RuntimeException("El libro no está disponible para préstamo");
            }
        } catch (Exception e) {
            if (e.getMessage().contains("no está disponible")) {
                throw e;
            }
            // Si catalogo-service no responde, igual permite el préstamo
        }
    }

    // Verifica si el usuario existe en usuarios-service
    private void verificarUsuario(Integer usuarioId) {
        try {
            webClient.get()
                .uri("http://localhost:8080/usuarios/" + usuarioId)
                .retrieve()
                .bodyToMono(String.class)
                .block();
        } catch (Exception e) {
            // Si usuarios-service no responde, igual permite el préstamo
        }
    }

    public List<Prestamo> listar() {
        return repository.findAll();
    }

    public Optional<Prestamo> buscarPorId(Integer id) {
        return repository.findById(id);
    }

    public List<Prestamo> buscarPorUsuario(Integer usuarioId) {
        return repository.findByUsuarioId(usuarioId);
    }

    public List<Prestamo> buscarPorEstado(String estado) {
        return repository.findByEstado(estado);
    }

    public List<Prestamo> prestamosActivosDeUsuario(Integer usuarioId) {
        return repository.findByUsuarioIdAndEstado(usuarioId, "activo");
    }

    public Prestamo crear(Prestamo prestamo) {
        // Reglas de negocio: verificar libro y usuario antes de crear
        verificarLibro(prestamo.getLibroId());
        verificarUsuario(prestamo.getUsuarioId());

        prestamo.setFechaPrestamo(LocalDate.now());
        prestamo.setFechaDevolucion(LocalDate.now().plusDays(7));
        prestamo.setEstado("activo");
        return repository.save(prestamo);
    }

    public Prestamo devolver(Integer id) {
        Prestamo prestamo = repository.findById(id)
            .orElseThrow(() -> new RuntimeException("Préstamo no encontrado con id: " + id));
        prestamo.setFechaDevolucionReal(LocalDate.now());
        if (LocalDate.now().isAfter(prestamo.getFechaDevolucion())) {
            prestamo.setEstado("devuelto con retraso");
        } else {
            prestamo.setEstado("devuelto");
        }
        return repository.save(prestamo);
    }

    public Prestamo actualizar(Integer id, Prestamo datos) {
        Prestamo prestamo = repository.findById(id)
            .orElseThrow(() -> new RuntimeException("Préstamo no encontrado con id: " + id));
        prestamo.setEstado(datos.getEstado());
        prestamo.setObservaciones(datos.getObservaciones());
        return repository.save(prestamo);
    }

    public void eliminar(Integer id) {
        repository.deleteById(id);
    }
}