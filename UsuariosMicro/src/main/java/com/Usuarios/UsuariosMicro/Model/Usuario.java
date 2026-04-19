package com.Usuarios.UsuariosMicro.Model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "usuarios")
@Data
@NoArgsConstructor
@AllArgsConstructor

public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotBlank(message = "El nombre no puede estar vacío")
    @Column(nullable = false,length = 100)
    private String nombre;

    @NotBlank(message = "El apellido no puede estar vacío")
    @Column(nullable = false,length = 100)
    private String apellido;

    @NotBlank(message = "El correo no puede estar vacío")
    @Email(message = "El correo debe ser válido")
    @Column(nullable = false, unique = true,length = 150)
    private String correo;

    //esto lo vemos luego para cuando tengamos la dependencia de security"
    
    /*@NotBlank(message = "la contraseña no puede estar vacia")
    @Column(nullable = false,length = 255)
    private String contraseña; */

    @Column(length =20)
    private String telefono;

    @Column(length = 200)
    private String direccion;

    @NotBlank(message = "el no puede estar vacio")
    @Column(nullable = false,length = 20)
    //valores posibles : admin, bibliotecario, usuario o lector
    private String rol;

    @Column(nullable = false, length = 20)
     private String estado; //valores posibles: activo, inactivo o suspendido

}
