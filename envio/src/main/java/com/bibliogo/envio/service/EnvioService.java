package com.bibliogo.envio.service;

import com.bibliogo.envio.dto.EnvioRequestDTO;
import com.bibliogo.envio.dto.EnvioResponseDTO;
import com.bibliogo.envio.execption.ConflictoException;
import com.bibliogo.envio.execption.RecursoNoEncontradoException;
import com.bibliogo.envio.execption.ServicioNoDisponibleException;
import com.bibliogo.envio.model.Envio;
import com.bibliogo.envio.repository.EnvioRepository;

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
public class EnvioService {

    @Autowired
    private EnvioRepository repository;

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

    public List<Envio> listar() {
        log.info("Listando todos los envíos");
        return repository.findAll();
    }

    public Envio buscarPorId(Integer id) {
        log.info("Buscando envío con id: {}", id);
        return repository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Envío no encontrado con id: {}", id);
                    return new RecursoNoEncontradoException("Envío no encontrado con id: " + id);
                });
    }

    public List<Envio> buscarPorUsuario(Integer usuarioId) {
        log.info("Buscando envíos del usuario id: {}", usuarioId);
        return repository.findByUsuarioId(usuarioId);
    }

    public List<Envio> buscarPorPrestamo(Integer prestamoId) {
        log.info("Buscando envíos del préstamo id: {}", prestamoId);
        return repository.findByPrestamoId(prestamoId);
    }

    public List<Envio> buscarPorEstado(String estado) {
        log.info("Buscando envíos con estado: {}", estado);
        return repository.findByEstado(estado);
    }

    public EnvioResponseDTO crear(EnvioRequestDTO dto) {
        log.info("Creando envío — préstamo: {} usuario: {} comuna: {}",
                dto.getPrestamoId(), dto.getUsuarioId(), dto.getComuna());

        verificarPrestamo(dto.getPrestamoId());

        Envio envio = new Envio();
        envio.setPrestamoId(dto.getPrestamoId());
        envio.setUsuarioId(dto.getUsuarioId());
        envio.setDireccion(dto.getDireccion());
        envio.setComuna(dto.getComuna());
        envio.setEstado("pendiente");
        envio.setFechaCreacion(LocalDateTime.now());

        Envio guardado = repository.save(envio);

        log.info("Envío creado con id: {}", guardado.getId());
        return convertirDTO(guardado);
    }

    public EnvioResponseDTO despachar(Integer id) {
        log.info("Despachando envío id: {}", id);

        Envio envio = buscarPorId(id);

        if (envio.getEstado().equalsIgnoreCase("entregado")) {
            log.warn("Intento de despachar envío id: {} que ya fue entregado", id);
            throw new ConflictoException(
                    "No se puede despachar un envío que ya fue entregado"
            );
        }

        if (envio.getEstado().equalsIgnoreCase("en camino")) {
            log.warn("Envío id: {} ya está en camino", id);
            throw new ConflictoException(
                    "El envío ya está en camino"
            );
        }

        envio.setEstado("en camino");

        Envio actualizado = repository.save(envio);

        log.info("Envío id: {} despachado — estado: en camino", id);
        return convertirDTO(actualizado);
    }

    public EnvioResponseDTO entregar(Integer id) {
        log.info("Marcando envío id: {} como entregado", id);

        Envio envio = buscarPorId(id);

        if (envio.getEstado().equalsIgnoreCase("entregado")) {
            log.warn("Envío id: {} ya fue entregado previamente", id);
            throw new ConflictoException(
                    "El envío ya fue entregado"
            );
        }

        envio.setEstado("entregado");
        envio.setFechaEntrega(LocalDateTime.now());

        Envio actualizado = repository.save(envio);

        log.info("Envío id: {} entregado correctamente", id);
        return convertirDTO(actualizado);
    }

    public void eliminar(Integer id) {
        log.info("Eliminando envío con id: {}", id);

        if (!repository.existsById(id)) {
            log.warn("Envío no encontrado con id: {}", id);
            throw new RecursoNoEncontradoException(
                    "Envío no encontrado con id: " + id
            );
        }

        repository.deleteById(id);
        log.info("Envío con id: {} eliminado correctamente", id);
    }

    private EnvioResponseDTO convertirDTO(Envio envio) {
        return new EnvioResponseDTO(
                envio.getId(),
                envio.getPrestamoId(),
                envio.getUsuarioId(),
                envio.getDireccion(),
                envio.getComuna(),
                envio.getEstado(),
                envio.getFechaCreacion(),
                envio.getFechaEntrega()
        );
    }
}