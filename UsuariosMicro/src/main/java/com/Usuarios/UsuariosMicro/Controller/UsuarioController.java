package com.Usuarios.UsuariosMicro.Controller;

import com.Usuarios.UsuariosMicro.Model.Usuario;
import com.Usuarios.UsuariosMicro.Service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioService service;

    @GetMapping("/listar")
    public List<Usuario> listar() {
        return service.listar();
    }

    @GetMapping("/{id}")
    public Optional<Usuario> buscarPorId(@PathVariable Integer id) {
        return service.buscarPorId(id);
    }

    @GetMapping("/correo/{correo}")
    public Optional<Usuario> buscarPorCorreo(@PathVariable String correo) {
        return service.buscarPorCorreo(correo);
    }

    @GetMapping("/rol/{rol}")
    public List<Usuario> buscarPorRol(@PathVariable String rol) {
        return service.buscarPorRol(rol);
    }

    @GetMapping("/estado/{estado}")
    public List<Usuario> buscarPorEstado(@PathVariable String estado) {
        return service.buscarPorEstado(estado);
    }

    @PostMapping("/crear")
    public Usuario crear(@RequestBody Usuario usuario) {
        return service.crear(usuario);
    }

    @PutMapping("/actualizar/{id}")
    public Usuario actualizar(@PathVariable Integer id, @RequestBody Usuario usuario) {
        return service.actualizar(id, usuario);
    }

    @DeleteMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Integer id) {
        service.eliminar(id);
        return "Usuario eliminado correctamente";
    }
}