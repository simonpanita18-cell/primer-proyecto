package com.bibliogo.pago.repository;

import com.bibliogo.pago.model.Pago;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PagoRepository extends JpaRepository<Pago, Integer> {

    List<Pago> findByUsuarioId(Integer usuarioId);

    List<Pago> findByPrestamoId(Integer prestamoId);

    List<Pago> findByEstado(String estado);

    List<Pago> findByTipo(String tipo);
}