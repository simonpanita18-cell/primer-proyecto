package com.bibliogo.notificaciones.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bibliogo.notificaciones.dto.NotificacionRequestDTO;
import com.bibliogo.notificaciones.dto.NotificacionResponseDTO;
import com.bibliogo.notificaciones.exception.ConflictoException;
import com.bibliogo.notificaciones.exception.RecursoNoEncontradoException;
import com.bibliogo.notificaciones.model.Notificacion;
import com.bibliogo.notificaciones.repository.NotificacionRepository;

import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
public class NotificacionService {

    @Autowired
    private NotificacionRepository repository;

    public List<Notificacion> listar() {
        log.info("Listando todas las notificaciones");
        return repository.findAll();
    }

    public Notificacion buscarPorId(Integer id) {
        log.info("Buscando notificación con id: {}", id);
        return repository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Notificación no encontrada con id: {}", id);
                    return new RecursoNoEncontradoException("Notificación no encontrada con id: " + id);
                });
    }

    public List<Notificacion> buscarPorUsuario(Integer usuarioId) {
        log.info("Buscando notificaciones del usuario id: {}", usuarioId);
        return repository.findByUsuarioId(usuarioId);
    }

    public List<Notificacion> buscarPorEstado(String estado) {
        log.info("Buscando notificaciones con estado: {}", estado);
        return repository.findByEstado(estado);
    }

    public List<Notificacion> buscarPorTipo(String tipo) {
        log.info("Buscando notificaciones de tipo: {}", tipo);
        return repository.findByTipo(tipo);
    }

    public List<Notificacion> buscarNoLeidas(Integer usuarioId) {
        log.info("Buscando notificaciones no leídas del usuario id: {}", usuarioId);
        return repository.findByUsuarioIdAndEstado(usuarioId, "pendiente");
    }

    public NotificacionResponseDTO crear(NotificacionRequestDTO dto) {
        log.info("Creando notificación — usuario: {} tipo: {}", dto.getUsuarioId(), dto.getTipo());

        Notificacion notificacion = new Notificacion();
        notificacion.setUsuarioId(dto.getUsuarioId());
        notificacion.setTipo(dto.getTipo());
        notificacion.setMensaje(dto.getMensaje());
        notificacion.setEstado("pendiente");
        notificacion.setCreadoEn(LocalDateTime.now());

        Notificacion guardada = repository.save(notificacion);

        log.info("Notificación creada con id: {}", guardada.getId());
        return convertirDTO(guardada);
    }

    public NotificacionResponseDTO marcarComoLeida(Integer id) {
        log.info("Marcando notificación id: {} como leída", id);

        Notificacion notificacion = buscarPorId(id);

        if (notificacion.getEstado().equalsIgnoreCase("leida")) {
            log.warn("Notificación id: {} ya estaba marcada como leída", id);
            throw new ConflictoException("La notificación ya fue marcada como leída");
        }

        notificacion.setEstado("leida");

        Notificacion actualizada = repository.save(notificacion);

        log.info("Notificación id: {} marcada como leída correctamente", id);
        return convertirDTO(actualizada);
    }

    public void eliminar(Integer id) {
        log.info("Eliminando notificación con id: {}", id);

        if (!repository.existsById(id)) {
            log.warn("Notificación no encontrada con id: {}", id);
            throw new RecursoNoEncontradoException(
                    "Notificación no encontrada con id: " + id
            );
        }

        repository.deleteById(id);
        log.info("Notificación con id: {} eliminada correctamente", id);
    }

    private NotificacionResponseDTO convertirDTO(Notificacion notificacion) {
        return new NotificacionResponseDTO(
                notificacion.getId(),
                notificacion.getUsuarioId(),
                notificacion.getTipo(),
                notificacion.getMensaje(),
                notificacion.getEstado(),
                notificacion.getCreadoEn()
        );
    }
}