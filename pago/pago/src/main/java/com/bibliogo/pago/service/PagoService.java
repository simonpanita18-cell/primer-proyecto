package com.bibliogo.pago.service;

import com.bibliogo.pago.dto.PagoRequestDTO;
import com.bibliogo.pago.dto.PagoResponseDTO;
import com.bibliogo.pago.exception.ConflictoException;
import com.bibliogo.pago.exception.RecursoNoEncontradoException;
import com.bibliogo.pago.exception.ServicioNoDisponibleException;
import com.bibliogo.pago.model.Pago;
import com.bibliogo.pago.repository.PagoRepository;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientRequestException;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
public class PagoService {

    @Autowired
    private PagoRepository repository;

    @Autowired
    private WebClient webClient;

    private void verificarPrestamo(Integer prestamoId) {
        log.info("Verificando préstamo id: {}", prestamoId);

        try {
            webClient.get()
                    .uri("http://localhost:8084/prestamos/" + prestamoId)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();

        } catch (WebClientResponseException.NotFound e) {
            log.warn("Préstamo no encontrado con id: {}", prestamoId);
            throw new RecursoNoEncontradoException(
                    "Préstamo no encontrado con id: " + prestamoId
            );

        } catch (WebClientRequestException e) {
            log.error("prestamo-service no disponible al verificar préstamo id: {}", prestamoId);
            throw new ServicioNoDisponibleException(
                    "prestamo-service no está disponible. No se pudo verificar el préstamo con id: " + prestamoId
            );

        } catch (Exception e) {
            log.error("Error inesperado al verificar préstamo id: {}", prestamoId, e);
            throw new ServicioNoDisponibleException(
                    "Error al comunicarse con prestamo-service para verificar el préstamo con id: " + prestamoId
            );
        }
    }

    public List<Pago> listar() {
        log.info("Listando todos los pagos");
        return repository.findAll();
    }

    public Pago buscarPorId(Integer id) {
        log.info("Buscando pago con id: {}", id);
        return repository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Pago no encontrado con id: {}", id);
                    return new RecursoNoEncontradoException("Pago no encontrado con id: " + id);
                });
    }

    public List<Pago> buscarPorUsuario(Integer usuarioId) {
        log.info("Buscando pagos del usuario id: {}", usuarioId);
        return repository.findByUsuarioId(usuarioId);
    }

    public List<Pago> buscarPorPrestamo(Integer prestamoId) {
        log.info("Buscando pagos del préstamo id: {}", prestamoId);
        return repository.findByPrestamoId(prestamoId);
    }

    public List<Pago> buscarPorEstado(String estado) {
        log.info("Buscando pagos con estado: {}", estado);
        return repository.findByEstado(estado);
    }

    public List<Pago> buscarPorTipo(String tipo) {
        log.info("Buscando pagos de tipo: {}", tipo);
        return repository.findByTipo(tipo);
    }

    public PagoResponseDTO crear(PagoRequestDTO dto) {
        log.info("Creando pago — préstamo: {} usuario: {} monto: {}",
                dto.getPrestamoId(), dto.getUsuarioId(), dto.getMonto());

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

        log.info("Pago creado con id: {} — estado: pendiente", guardado.getId());
        return convertirDTO(guardado);
    }

    public PagoResponseDTO confirmar(Integer id) {
        log.info("Confirmando pago id: {}", id);

        Pago pago = buscarPorId(id);

        if (pago.getEstado().equalsIgnoreCase("pagado")) {
            log.warn("Pago id: {} ya fue confirmado previamente", id);
            throw new ConflictoException("El pago ya fue confirmado");
        }

        if (pago.getEstado().equalsIgnoreCase("rechazado")) {
            log.warn("Intento de confirmar pago id: {} que ya fue rechazado", id);
            throw new ConflictoException("No se puede confirmar un pago rechazado");
        }

        pago.setEstado("pagado");

        Pago actualizado = repository.save(pago);

        log.info("Pago id: {} confirmado correctamente", id);
        return convertirDTO(actualizado);
    }

    public PagoResponseDTO rechazar(Integer id) {
        log.info("Rechazando pago id: {}", id);

        Pago pago = buscarPorId(id);

        if (pago.getEstado().equalsIgnoreCase("pagado")) {
            log.warn("Intento de rechazar pago id: {} que ya fue confirmado", id);
            throw new ConflictoException("No se puede rechazar un pago ya confirmado");
        }

        if (pago.getEstado().equalsIgnoreCase("rechazado")) {
            log.warn("Pago id: {} ya fue rechazado previamente", id);
            throw new ConflictoException("El pago ya fue rechazado");
        }

        pago.setEstado("rechazado");

        Pago actualizado = repository.save(pago);

        log.info("Pago id: {} rechazado correctamente", id);
        return convertirDTO(actualizado);
    }

    public void eliminar(Integer id) {
        log.info("Eliminando pago con id: {}", id);

        if (!repository.existsById(id)) {
            log.warn("Pago no encontrado con id: {}", id);
            throw new RecursoNoEncontradoException("Pago no encontrado con id: " + id);
        }

        repository.deleteById(id);
        log.info("Pago con id: {} eliminado correctamente", id);
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