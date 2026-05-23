package com.bibliogo.carrito.Repository;

import com.bibliogo.carrito.Model.Carrito;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface CarritoRepository extends JpaRepository<Carrito, Integer> {
    List<Carrito> findByUsuarioId(Integer usuarioId);
    List<Carrito> findByEstado(String estado);
    List<Carrito> findByUsuarioIdAndEstado(Integer usuarioId, String estado);
}