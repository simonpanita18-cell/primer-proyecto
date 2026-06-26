# 📚 BiblioGo

<div align="center">

## Sistema de Gestión Bibliotecaria basado en Arquitectura de Microservicios

**Examen Transversal – Ingeniería de Software**

**Duoc UC**

---

![Java](https://img.shields.io/badge/Java-21-orange)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.4.1-green)
![MySQL](https://img.shields.io/badge/MySQL-8.0-blue)
![Architecture](https://img.shields.io/badge/Microservices-9-red)
![Status](https://img.shields.io/badge/Estado-Finalizado-success)

</div>

---

# 👨‍💻 Desarrollador

**Simón Eduardo Hércules Márquez**

Carrera: Analista Programador

Institución: Duoc UC

Asignatura: Ingeniería de Software

Docente: Osnellys Andrade

---

# 📖 Descripción

BiblioGo es un sistema de gestión bibliotecaria desarrollado bajo una arquitectura basada en microservicios utilizando Spring Boot. Su objetivo es automatizar los procesos de administración de bibliotecas, permitiendo gestionar usuarios, catálogo de libros, préstamos, pagos, notificaciones, reseñas y reportes de manera independiente, escalable y mantenible.

El proyecto fue desarrollado siguiendo buenas prácticas de Ingeniería de Software, aplicando principios de arquitectura limpia, control de versiones, modelado UML, prototipado en Figma y validación mediante estándares de calidad ISO/IEC 25010.

---

# 🎯 Objetivos

* Automatizar la gestión bibliotecaria.
* Optimizar el control de préstamos y devoluciones.
* Mejorar la experiencia de usuarios y administradores.
* Implementar una arquitectura desacoplada mediante microservicios.
* Facilitar futuras ampliaciones del sistema.

---

# 🏗 Arquitectura

La solución está compuesta por nueve microservicios independientes, cada uno con responsabilidades claramente definidas.

```
Cliente
      │
      ▼
API Gateway
      │
 ┌────┼─────────────────────────────┐
 ▼    ▼      ▼      ▼      ▼
Usuarios  Catálogo  Carrito  Préstamos  Pagos
                    │
                    ▼
        Notificaciones • Reseñas • Reportes
```

La comunicación entre servicios se realiza mediante APIs REST utilizando **Spring WebClient**, mientras que el descubrimiento de servicios se gestiona mediante **Eureka Server**.

---

# ⚙ Tecnologías Utilizadas

* Java 21
* Spring Boot 3.4.1
* Spring Data JPA
* Spring Web
* Spring Cloud Gateway
* Eureka Server
* WebClient
* MySQL
* Maven
* Lombok
* Bean Validation
* BCrypt
* Git
* GitHub
* Trello
* Figma
* Postman

---

# 📦 Microservicios

| Servicio         | Puerto | Responsabilidad                     |
| ---------------- | ------ | ----------------------------------- |
| UsuariosMicro    | 8081   | Gestión de usuarios y autenticación |
| Catalogo-Service | 8082   | Administración del catálogo         |
| Carrito          | 8083   | Gestión del carrito                 |
| Prestamo         | 8084   | Administración de préstamos         |
| Envío            | 8085   | Gestión de envíos                   |
| Pago             | 8086   | Administración de pagos             |
| Notificaciones   | 8087   | Gestión de notificaciones           |
| Reseña           | 8088   | Calificaciones y comentarios        |
| Reporte          | 8089   | Reportes administrativos            |

---

# 🚀 Funcionalidades

### Usuario

* Registro de usuario
* Inicio de sesión
* Consulta de catálogo
* Solicitud de préstamos
* Devolución de libros
* Consulta de historial
* Gestión del perfil

### Administrador

* Gestión de usuarios
* Gestión del catálogo
* Administración de préstamos
* Control de disponibilidad
* Administración de reportes
* Gestión de libros

---

# 🔗 Comunicación entre Microservicios

* Prestamo → Catalogo: validación de existencia y disponibilidad del libro.
* Prestamo → Usuarios: validación de existencia del usuario.
* Carrito → Catalogo: validación de disponibilidad antes de agregar un libro.
* Pago → Prestamo: validación del préstamo asociado.
* Notificaciones → Usuarios: envío de avisos al usuario.

---

# 📋 Reglas de Negocio

* Reducción automática del stock al generar un préstamo.
* Cambio automático del estado de disponibilidad.
* Contraseñas protegidas mediante BCrypt.
* Fecha de devolución generada automáticamente.
* Cambio de estado por retraso en devoluciones.
* Validación de reseñas con puntuación entre 1 y 5.

---

# 🧪 Calidad del Software

Durante el desarrollo se aplicaron los siguientes estándares y herramientas:

* ISO/IEC 25010
* Heurísticas de Nielsen
* UML (Modelo 4+1)
* Casos de Uso
* Plan de Pruebas
* Figma
* GitHub
* Trello

---

# 🔄 Control de Versiones

El proyecto utiliza Git y GitHub para registrar la evolución del código mediante versionamiento semántico.

```
v1.0.0
v2.0.0
v3.0.0
```

La planificación y seguimiento del proyecto fueron gestionados mediante Trello bajo metodología Scrum.

---

# ▶️ Ejecución

## Requisitos

* Java 21
* Maven
* MySQL

## Orden de ejecución

1. Eureka Server
2. API Gateway
3. UsuariosMicro
4. Catalogo-Service
5. Carrito
6. Prestamo
7. Pago
8. Envío
9. Notificaciones
10. Reseñas
11. Reportes

Posteriormente, los endpoints pueden ser validados mediante Postman.

---

# 📁 Organización del Proyecto

```
BiblioGo
│
├── api-gateway
├── eureka-server
├── UsuariosMicro
├── catalogo-service
├── carrito
├── prestamo
├── pago
├── envio
├── notificaciones
├── resena
├── reporte
└── README.md
```

---

# 🔮 Trabajo Futuro

* Implementación de autenticación JWT.
* Despliegue en servicios Cloud.
* Dashboard analítico.
* Sistema de reservas.
* Aplicación móvil.
* Recomendaciones inteligentes mediante IA.

---

# 📜 Licencia

Proyecto desarrollado con fines académicos para el Examen Transversal de Ingeniería de Software de Duoc UC.
