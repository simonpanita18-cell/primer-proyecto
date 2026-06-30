# 📚 BiblioGo

<div align="center">

## Sistema de Gestión Bibliotecaria basado en Arquitectura de Microservicios

**Examen Transversal – Ingeniería de Software**

**Duoc UC**

---

![Java](https://img.shields.io/badge/Java-21-orange)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.4.1-green)
![MySQL](https://img.shields.io/badge/MySQL-8.4-blue)
![Architecture](https://img.shields.io/badge/Microservices-9-red)
![Status](https://img.shields.io/badge/Estado-Finalizado-success)

</div>

---

# 👨‍💻 Desarrollador

**Simón Eduardo Hércules Márquez**
* **Carrera:** Analista Programador
* **Institución:** Duoc UC
* **Asignatura:** Ingeniería de Software
* **Docente:** Osnellys Andrade

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

La solución está compuesta por un servidor de descubrimiento, un enrutador central y nueve microservicios independientes, cada uno con responsabilidades claramente definidas.

Cliente
│
▼
API Gateway (Puerto 8090)
│
┌────┼─────────────────────────────┐
▼    ▼      ▼      ▼      ▼
Usuarios  Catálogo  Carrito  Préstamos  Pagos
│
▼
Notificaciones • Reseñas • Reportes


La comunicación entre servicios se realiza mediante APIs REST utilizando **Spring WebClient**, mientras que el descubrimiento de servicios se gestiona dinámicamente mediante **Eureka Server**.

---

# ⚙ Tecnologías Utilizadas

* Java 21
* Spring Boot 3.4.1
* Spring Data JPA
* Spring Cloud Gateway
* Eureka Server
* WebClient
* MySQL 8.4 (Dockerizado)
* Maven
* Lombok
* Bean Validation
* BCrypt
* Git / GitHub
* Docker / Docker Compose
* Trello
* Figma
* Postman

---

# 📦 Infraestructura y Microservicios (Puertos Actualizados)

| Componente / Servicio | Puerto Externo | Puerto Interno | Responsabilidad |
| :--- | :---: | :---: | :--- |
| **MySQL (Base de Datos)** | `3307` | `3306` | Almacenamiento relacional de datos persistentes |
| **Eureka Server** | `8761` | `8761` | Servidor de descubrimiento de servicios |
| **API Gateway** | `8090` | `8090` | Enrutador centralizado y punto único de entrada |
| **UsuariosMicro** | `8081` | `8081` | Gestión de usuarios, roles y autenticación |
| **Catalogo-Service** | `8082` | `8082` | Administración del catálogo de libros y stock |
| **Carrito** | `8083` | `8083` | Gestión temporal de elementos a reservar/prestar |
| **Prestamo** | `8084` | `8084` | Administración del ciclo de préstamos y devoluciones |
| **Pago** | `8085` | `8085` | Procesamiento y registro de transacciones de pago |
| **Notificaciones** | `8086` | `8086` | Despacho de avisos de estado y alertas al usuario |
| **Envío** | `8087` | `8087` | Gestión de logística de envíos físicos de libros |
| **Reseña** | `8088` | `8088` | Gestión de calificaciones (1-5) y comentarios |
| **Reporte** | `8089` | `8089` | Consolidación de métricas e informes administrativos |

---

# 🚀 Funcionalidades Principales

### Rol: Usuario
* Registro e inicio de sesión seguro (Contraseñas con hash BCrypt).
* Consulta interactiva del catálogo disponible.
* Gestión de carrito de solicitudes y generación de préstamos.
* Seguimiento e historial de estados físicos del envío y devoluciones.
* Retroalimentación mediante calificaciones y comentarios en libros leídos.

### Rol: Administrador
* Gestión integral del ciclo de vida de usuarios y catálogo de stock.
* Control total y penalizaciones sobre retrasos en devoluciones automáticas.
* Visualización y exportación de reportes administrativos y de uso del sistema.

---

# 📋 Reglas de Negocio Implementadas

* Reducción automática del stock del Catálogo al confirmarse un préstamo.
* Bloqueo transaccional de libros si la disponibilidad física es cero.
* Generación automatizada de fechas límite de devolución calculadas por el sistema.
* Validación estricta a nivel controlador de reseñas (rango numérico obligatorio entre 1 y 5).

---

# 🧪 Calidad del Software

Durante el desarrollo se aplicaron los siguientes estándares y herramientas:
* **ISO/IEC 25010:** Evaluación de características de mantenibilidad, compatibilidad y seguridad.
* **UML (Modelo 4+1):** Diseño arquitectónico detallado a nivel de casos de uso y diagramas de componentes.
* **Pruebas Unitarias:** Implementación con JUnit y Mockito para validación de la lógica crítica.

---

# ▶️ Despliegue y Execution con Docker Compose

## Requisitos Previos
* Docker Desktop activo en la máquina anfitriona.
* Terminal de comandos (PowerShell / CMD / Bash).

## Orquestación del Entorno (Orden Automático)
Gracias al uso de las directivas `depends_on` con criterios de salud (`service_healthy`), la infraestructura se autogestiona en el siguiente orden estricto de resiliencia:

1. **mysql-bibliogo** (Espera a pasar el control de salud mediante `mysqladmin ping`).
2. **eureka-server** (Inicia inmediatamente después del motor SQL).
3. **api-gateway y Microservicios** (Arrancan en paralelo una vez detectados los servicios base).

Para compilar las imágenes locales (utilizando sus respectivos `Dockerfile`) y encender la red completa en segundo plano, ejecute desde la raíz del proyecto:

```bash
docker compose up -d --build
Para verificar la operatividad y salud de los contenedores:

Bash
docker compose ps
📁 Organización del Repositorio
Plaintext
BiblioGo
├── docker-compose.yml
├── init-db.sql
├── api-gateway/
├── eureka-server/
├── UsuariosMicro/
├── catalogo-service/
├── carrito/
├── prestamo/
├── pago/
├── notificaciones/
├── envio/
├── resena/
├── reporte/
└── README.md
📜 Licencia
Proyecto desarrollado con fines académicos para el Examen Transversal de Ingeniería de Software de Duoc UC. All rights reserved © 2026.