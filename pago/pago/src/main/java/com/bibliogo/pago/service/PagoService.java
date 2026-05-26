package com.bibliogo.pago.service;

import com.bibliogo.pago.dto.PagoRequestDTO;
import com.bibliogo.pago.dto.PagoResponseDTO;
import com.bibliogo.pago.exception.ConflictoException;
import com.bibliogo.pago.exception.RecursoNoEncontradoException;
import com.bibliogo.pago.exception.ServicioNoDisponibleException;
import com.bibliogo.pago.model.Pago;
import com.bibliogo.pago.repository.PagoRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientRequestException;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class PagoService {

    @Autowired
    private PagoRepository repository;

    @Autowired
    private WebClient webClient;

    private void verificarPrestamo(Integer prestamoId) {

        try {
            webClient.get()
                    .uri("http://localhost:8084/prestamos/" + prestamoId)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();

        } catch (WebClientResponseException.NotFound e) {
            throw new RecursoNoEncontradoException(
                    "Préstamo no encontrado con id: " + prestamoId
            );

        } catch (WebClientRequestException e) {
            throw new ServicioNoDisponibleException(
                    "prestamo-service no está disponible. No se pudo verificar el préstamo con id: " + prestamoId
            );

        } catch (Exception e) {
            throw new ServicioNoDisponibleException(
                    "Error al comunicarse con prestamo-service para verificar el préstamo con id: " + prestamoId
            );
        }
    }

    public List<Pago> listar() {
        return repository.findAll();
    }

    public Pago buscarPorId(Integer id) {
        return repository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Pago no encontrado con id: " + id));
    }

    public List<Pago> buscarPorUsuario(Integer usuarioId) {
        return repository.findByUsuarioId(usuarioId);
    }

    public List<Pago> buscarPorPrestamo(Integer prestamoId) {
        return repository.findByPrestamoId(prestamoId);
    }

    public List<Pago> buscarPorEstado(String estado) {
        return repository.findByEstado(estado);
    }

    public List<Pago> buscarPorTipo(String tipo) {
        return repository.findByTipo(tipo);
    }

    public PagoResponseDTO crear(PagoRequestDTO dto) {

        verificarPrestamo(dto.getPrestamoId());

        Pago pago = new Pago();

        pago.setPrestamoId(dto.getPrestamoId());
        pago.setUsuarioId(dto.getUsuarioId());
        pago.setMonto(dto.getMonto());
        pago.setMetodo(dto.getMetodo());
        pago.setTipo(dto.getTipo());
        pago.setEstado("pendiente");
        pago.setFechaPago(LocalDateTime.now());

        Pago guardado = repository.save(pago);

        return convertirDTO(guardado);
    }

    public PagoResponseDTO confirmar(Integer id) {

        Pago pago = buscarPorId(id);

        if (pago.getEstado().equalsIgnoreCase("pagado")) {
            throw new ConflictoException("El pago ya fue confirmado");
        }

        if (pago.getEstado().equalsIgnoreCase("rechazado")) {
            throw new ConflictoException("No se puede confirmar un pago rechazado");
        }

        pago.setEstado("pagado");

        Pago actualizado = repository.save(pago);

        return convertirDTO(actualizado);
    }

    public PagoResponseDTO rechazar(Integer id) {

        Pago pago = buscarPorId(id);

        if (pago.getEstado().equalsIgnoreCase("pagado")) {
            throw new ConflictoException("No se puede rechazar un pago ya confirmado");
        }

        if (pago.getEstado().equalsIgnoreCase("rechazado")) {
            throw new ConflictoException("El pago ya fue rechazado");
        }

        pago.setEstado("rechazado");

        Pago actualizado = repository.save(pago);

        return convertirDTO(actualizado);
    }

    public void eliminar(Integer id) {

        if (!repository.existsById(id)) {
            throw new RecursoNoEncontradoException("Pago no encontrado con id: " + id);
        }

        repository.deleteById(id);
    }

    private PagoResponseDTO convertirDTO(Pago pago) {

        return new PagoResponseDTO(
                pago.getId(),
                pago.getPrestamoId(),
                pago.getUsuarioId(),
                pago.getMonto(),
                pago.getMetodo(),
                pago.getEstado(),
                pago.getTipo(),
                pago.getFechaPago()
        );
    }
}