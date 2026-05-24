package com.bibliogo.notificacion.service;

import com.bibliogo.notificacion.model.Notificacion;
import com.bibliogo.notificacion.repository.NotificacionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
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

    public Notificacion crear(Notificacion notificacion) {
        notificacion.setEstado("pendiente");
        notificacion.setCreadoEn(LocalDateTime.now());
        return repository.save(notificacion);
    }

    public Notificacion marcarComoLeida(Integer id) {
        Notificacion notificacion = buscarPorId(id);
        notificacion.setEstado("leida");
        return repository.save(notificacion);
    }

    public void eliminar(Integer id) {
        if (!repository.existsById(id)) {
            throw new RuntimeException("Notificación no encontrada con id: " + id);
        }
        repository.deleteById(id);
    }
}