package com.bibliogo.envio.service;

import com.bibliogo.envio.model.Envio;
import com.bibliogo.envio.repository.EnvioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class EnvioService {

    @Autowired
    private EnvioRepository repository;

    public List<Envio> listar() {
        return repository.findAll();
    }

    public Optional<Envio> buscarPorId(Integer id) {
        return repository.findById(id);
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

    public Envio crear(Envio envio) {
        envio.setEstado("pendiente");
        envio.setFechaCreacion(LocalDateTime.now());
        return repository.save(envio);
    }

    public Envio despachar(Integer id) {
        Envio envio = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Envío no encontrado con id: " + id));

        envio.setEstado("en camino");
        return repository.save(envio);
    }

    public Envio entregar(Integer id) {
        Envio envio = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Envío no encontrado con id: " + id));

        envio.setEstado("entregado");
        envio.setFechaEntrega(LocalDateTime.now());
        return repository.save(envio);
    }

    public void eliminar(Integer id) {
        repository.deleteById(id);
    }
}