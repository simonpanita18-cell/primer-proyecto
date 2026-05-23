package com.Usuarios.UsuariosMicro.Service;

import com.Usuarios.UsuariosMicro.Model.Usuario;
import com.Usuarios.UsuariosMicro.Repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository repository;

    public List<Usuario> listar() {
        return repository.findAll();
    }

    public Optional<Usuario> buscarPorId(Integer id) {
        return repository.findById(id);
    }

    public Optional<Usuario> buscarPorCorreo(String correo) {
        return repository.findByCorreoIgnoreCase(correo);
    }

    public List<Usuario> buscarPorRol(String rol) {
        return repository.findByRol(rol);
    }

    public List<Usuario> buscarPorEstado(String estado) {
        return repository.findByEstado(estado);
    }

    public Usuario crear(Usuario usuario) {
        usuario.setEstado("activo");
        return repository.save(usuario);
    }

    public Usuario actualizar(Integer id, Usuario datos) {
        Usuario usuario = repository.findById(id)
            .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        usuario.setNombre(datos.getNombre());
        usuario.setApellido(datos.getApellido());
        usuario.setTelefono(datos.getTelefono());
        usuario.setDireccion(datos.getDireccion());
        usuario.setRol(datos.getRol());
        usuario.setEstado(datos.getEstado());
        return repository.save(usuario);
    }

    public void eliminar(Integer id) {
        repository.deleteById(id);
    }
}