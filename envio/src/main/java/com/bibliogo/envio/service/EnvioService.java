package com.bibliogo.envio.service;

import com.bibliogo.envio.dto.EnvioRequestDTO;
import com.bibliogo.envio.dto.EnvioResponseDTO;
import com.bibliogo.envio.execption.ConflictoException;
import com.bibliogo.envio.execption.RecursoNoEncontradoException;
import com.bibliogo.envio.execption.ServicioNoDisponibleException;
import com.bibliogo.envio.model.Envio;
import com.bibliogo.envio.repository.EnvioRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientRequestException;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class EnvioService {

    @Autowired
    private EnvioRepository repository;

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

    public List<Envio> listar() {
        return repository.findAll();
    }

    public Envio buscarPorId(Integer id) {
        return repository.findById(id)
                .orElseThrow(() ->
                        new RecursoNoEncontradoException("Envío no encontrado con id: " + id));
    }

    public List<Envio> buscarPorUsuario(Integer usuarioId) {
        return repository.findByUsuarioId(usuarioId);
    }

    public List<Envio> buscarPorPrestamo(Integer prestamoId) {
        return repository.findByPrestamoId(prestamoId);
    }

    public List<Envio> buscarPorEstado(String estado) {
        return repository.findByEstado(estado);
    }

    public EnvioResponseDTO crear(EnvioRequestDTO dto) {

        verificarPrestamo(dto.getPrestamoId());

        Envio envio = new Envio();

        envio.setPrestamoId(dto.getPrestamoId());
        envio.setUsuarioId(dto.getUsuarioId());
        envio.setDireccion(dto.getDireccion());
        envio.setComuna(dto.getComuna());
        envio.setEstado("pendiente");
        envio.setFechaCreacion(LocalDateTime.now());

        Envio guardado = repository.save(envio);

        return convertirDTO(guardado);
    }

    public EnvioResponseDTO despachar(Integer id) {

        Envio envio = buscarPorId(id);

        if (envio.getEstado().equalsIgnoreCase("entregado")) {
            throw new ConflictoException(
                    "No se puede despachar un envío que ya fue entregado"
            );
        }

        if (envio.getEstado().equalsIgnoreCase("en camino")) {
            throw new ConflictoException(
                    "El envío ya está en camino"
            );
        }

        envio.setEstado("en camino");

        Envio actualizado = repository.save(envio);

        return convertirDTO(actualizado);
    }

    public EnvioResponseDTO entregar(Integer id) {

        Envio envio = buscarPorId(id);

        if (envio.getEstado().equalsIgnoreCase("entregado")) {
            throw new ConflictoException(
                    "El envío ya fue entregado"
            );
        }

        envio.setEstado("entregado");
        envio.setFechaEntrega(LocalDateTime.now());

        Envio actualizado = repository.save(envio);

        return convertirDTO(actualizado);
    }

    public void eliminar(Integer id) {

        if (!repository.existsById(id)) {
            throw new RecursoNoEncontradoException(
                    "Envío no encontrado con id: " + id
            );
        }

        repository.deleteById(id);
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