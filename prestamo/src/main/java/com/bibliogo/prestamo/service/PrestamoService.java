package com.bibliogo.prestamo.service;

import com.bibliogo.prestamo.dto.PrestamoRequestDTO;
import com.bibliogo.prestamo.dto.PrestamoResponseDTO;
import com.bibliogo.prestamo.dto.PrestamoUpdateDTO;
import com.bibliogo.prestamo.model.Prestamo;
import com.bibliogo.prestamo.repository.PrestamoRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.LocalDate;
import java.util.List;

@Service
public class PrestamoService {

    @Autowired
    private PrestamoRepository repository;

    @Autowired
    private WebClient webClient;

    private void verificarLibro(Integer libroId) {
        try {
            String respuestaLibro = webClient.get()
                    .uri("http://localhost:8082/libros/" + libroId)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();

            if (respuestaLibro == null || respuestaLibro.contains("no disponible")) {
                throw new RuntimeException("El libro no está disponible para préstamo");
            }

        } catch (Exception e) {
            if (e.getMessage() != null && e.getMessage().contains("no está disponible")) {
                throw e;
            }
        }
    }

    private void verificarUsuario(Integer usuarioId) {
        try {
            webClient.get()
                    .uri("http://localhost:8081/usuarios/" + usuarioId)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();

        } catch (Exception e) {
            throw new RuntimeException("No se pudo verificar el usuario con id: " + usuarioId);
        }
    }

    public List<Prestamo> listar() {
        return repository.findAll();
    }

    public Prestamo buscarPorId(Integer id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Préstamo no encontrado con id: " + id));
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

   public PrestamoResponseDTO crear(PrestamoRequestDTO dto) {

    verificarLibro(dto.getLibroId());
    verificarUsuario(dto.getUsuarioId());

    Prestamo prestamo = new Prestamo();

    prestamo.setUsuarioId(dto.getUsuarioId());
    prestamo.setLibroId(dto.getLibroId());
    prestamo.setTituloLibro(dto.getTituloLibro());
    prestamo.setObservaciones(dto.getObservaciones());
    prestamo.setFechaPrestamo(LocalDate.now());
    prestamo.setFechaDevolucion(LocalDate.now().plusDays(7));
    prestamo.setEstado("activo");

    Prestamo guardado = repository.save(prestamo);

    return convertirDTO(guardado);
}

    public PrestamoResponseDTO devolver(Integer id) {

        Prestamo prestamo = buscarPorId(id);

        prestamo.setFechaDevolucionReal(LocalDate.now());

        if (LocalDate.now().isAfter(prestamo.getFechaDevolucion())) {
            prestamo.setEstado("devuelto con retraso");
        } else {
            prestamo.setEstado("devuelto");
        }

        Prestamo actualizado = repository.save(prestamo);

        return convertirDTO(actualizado);
    }

    public PrestamoResponseDTO actualizar(Integer id, PrestamoUpdateDTO dto) {

        Prestamo prestamo = buscarPorId(id);

        prestamo.setEstado(dto.getEstado());
        prestamo.setObservaciones(dto.getObservaciones());

        Prestamo actualizado = repository.save(prestamo);

        return convertirDTO(actualizado);
    }

    public void eliminar(Integer id) {
        if (!repository.existsById(id)) {
            throw new RuntimeException("Préstamo no encontrado con id: " + id);
        }
        repository.deleteById(id);
    }

    private PrestamoResponseDTO convertirDTO(Prestamo prestamo) {
        return new PrestamoResponseDTO(
                prestamo.getId(),
                prestamo.getUsuarioId(),
                prestamo.getLibroId(),
                prestamo.getTituloLibro(),
                prestamo.getFechaPrestamo(),
                prestamo.getFechaDevolucion(),
                prestamo.getFechaDevolucionReal(),
                prestamo.getEstado(),
                prestamo.getObservaciones()
        );
    }
}