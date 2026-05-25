package com.bibliogo.envio.service;

import com.bibliogo.envio.dto.EnvioRequestDTO;
import com.bibliogo.envio.dto.EnvioResponseDTO;
import com.bibliogo.envio.model.Envio;
import com.bibliogo.envio.repository.EnvioRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class EnvioService {

    @Autowired
    private EnvioRepository repository;

    public List<Envio> listar() {
        return repository.findAll();
    }

    public Envio buscarPorId(Integer id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Envío no encontrado con id: " + id));
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

        envio.setEstado("en camino");

        Envio actualizado = repository.save(envio);

        return convertirDTO(actualizado);
    }

    public EnvioResponseDTO entregar(Integer id) {

        Envio envio = buscarPorId(id);

        envio.setEstado("entregado");
        envio.setFechaEntrega(LocalDateTime.now());

        Envio actualizado = repository.save(envio);

        return convertirDTO(actualizado);
    }

    public void eliminar(Integer id) {

        if (!repository.existsById(id)) {
            throw new RuntimeException("Envío no encontrado con id: " + id);
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