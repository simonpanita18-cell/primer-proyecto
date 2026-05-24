package com.bibliogo.notificacion.repository;

import com.bibliogo.notificacion.model.Notificacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificacionRepository extends JpaRepository<Notificacion, Integer> {

    List<Notificacion> findByUsuarioId(Integer usuarioId);

    List<Notificacion> findByEstado(String estado);

    List<Notificacion> findByTipo(String tipo);

    List<Notificacion> findByUsuarioIdAndEstado(Integer usuarioId, String estado);
}