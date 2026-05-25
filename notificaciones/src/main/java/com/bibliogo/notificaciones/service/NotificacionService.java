package com.bibliogo.notificaciones.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bibliogo.notificaciones.dto.NotificacionRequestDTO;
import com.bibliogo.notificaciones.dto.NotificacionResponseDTO;
import com.bibliogo.notificaciones.model.Notificacion;
import com.bibliogo.notificaciones.repository.NotificacionRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class NotificacionService {

    @Autowired
    private NotificacionRepository repository;

    public List<Notificacion> listar() {
        return repository.findAll();
    }

    public Notificacion buscarPorId(Integer id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Notificación no encontrada con id: " + id));
    }

    public List<Notificacion> buscarPorUsuario(Integer usuarioId) {
        return repository.findByUsuarioId(usuarioId);
    }

    public List<Notificacion> buscarPorEstado(String estado) {
        return repository.findByEstado(estado);
    }

    public List<Notificacion> buscarPorTipo(String tipo) {
        return repository.findByTipo(tipo);
    }

    public List<Notificacion> buscarNoLeidas(Integer usuarioId) {
        return repository.findByUsuarioIdAndEstado(usuarioId, "pendiente");
    }

    public NotificacionResponseDTO crear(NotificacionRequestDTO dto) {

        Notificacion notificacion = new Notificacion();

        notificacion.setUsuarioId(dto.getUsuarioId());
        notificacion.setTipo(dto.getTipo());
        notificacion.setMensaje(dto.getMensaje());
        notificacion.setEstado("pendiente");
        notificacion.setCreadoEn(LocalDateTime.now());

        Notificacion guardada = repository.save(notificacion);

        return convertirDTO(guardada);
    }

    public NotificacionResponseDTO marcarComoLeida(Integer id) {

        Notificacion notificacion = buscarPorId(id);

        notificacion.setEstado("leida");

        Notificacion actualizada = repository.save(notificacion);

        return convertirDTO(actualizada);
    }

    public void eliminar(Integer id) {
        if (!repository.existsById(id)) {
            throw new RuntimeException("Notificación no encontrada con id: " + id);
        }
        repository.deleteById(id);
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