package com.Usuarios.UsuariosMicro.Model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

@Entity
@Table(name = "usuarios")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotBlank(message = "El nombre no puede estar vacío")
    @Column(nullable = false, length = 100)
    private String nombre;

    @NotBlank(message = "El apellido no puede estar vacío")
    @Column(nullable = false, length = 100)
    private String apellido;

    @NotBlank(message = "El correo no puede estar vacío")
    @Email(message = "El correo debe ser válido")
    @Column(nullable = false, unique = true, length = 150)
    private String correo;

    @Column(length = 20)
    private String telefono;

    @Column(length = 200)
    private String direccion;

    @NotBlank(message = "El rol no puede estar vacío")
    @Column(nullable = false, length = 20)
    private String rol;

    @Column(nullable = false, length = 20)
    private String estado;

    public Usuario() {}

    public Usuario(Integer id, String nombre, String apellido, String correo,
                   String telefono, String direccion, String rol, String estado) {
        this.id = id;
        this.nombre = nombre;
        this.apellido = apellido;
        this.correo = correo;
        this.telefono = telefono;
        this.direccion = direccion;
        this.rol = rol;
        this.estado = estado;
    }

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getApellido() { return apellido; }
    public void setApellido(String apellido) { this.apellido = apellido; }
    public String getCorreo() { return correo; }
    public void setCorreo(String correo) { this.correo = correo; }
    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }
    public String getDireccion() { return direccion; }
    public void setDireccion(String direccion) { this.direccion = direccion; }
    public String getRol() { return rol; }
    public void setRol(String rol) { this.rol = rol; }
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
}