package com.bibliogo.pago.service;

import com.bibliogo.pago.model.Pago;
import com.bibliogo.pago.repository.PagoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class PagoService {

    @Autowired
    private PagoRepository repository;

    public List<Pago> listar() {
        return repository.findAll();
    }

    public Pago buscarPorId(Integer id) {
        return repository.findById(id)
            .orElseThrow(() -> new RuntimeException("Pago no encontrado con id: " + id));
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

    public Pago crear(Pago pago) {
        pago.setEstado("pendiente");
        pago.setFechaPago(LocalDateTime.now());
        return repository.save(pago);
    }

    public Pago confirmar(Integer id) {
        Pago pago = buscarPorId(id);
        pago.setEstado("pagado");
        return repository.save(pago);
    }

    public Pago rechazar(Integer id) {
        Pago pago = buscarPorId(id);
        pago.setEstado("rechazado");
        return repository.save(pago);
    }

    public void eliminar(Integer id) {
        if (!repository.existsById(id)) {
            throw new RuntimeException("Pago no encontrado con id: " + id);
        }
        repository.deleteById(id);
    }
}