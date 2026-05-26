#  BiblioGo — Sistema de Gestión de Biblioteca

## Integrantes
- Simon Panita
- Simone Panita

## Descripción
BiblioGo es un sistema de gestión de biblioteca desarrollado con arquitectura de microservicios usando Spring Boot. Permite gestionar usuarios, libros, préstamos, pagos, envíos, reseñas, notificaciones y reportes de forma independiente y escalable.

## Tecnologías utilizadas
- Java 21
- Spring Boot 3.4.1
- MySQL
- Maven
- Lombok
- Spring Security + BCrypt
- Spring Validation (Bean Validation)
- WebClient (Spring WebFlux)
- Patrón CSR (Controller - Service - Repository)
- DTOs (Request y Response)
- GitHub

## Microservicios implementados

| Microservicio | Puerto | Base de datos | Descripción |
|---|---|---|---|
| UsuariosMicro | 8081 | bibliogo_usuarios | Gestión de usuarios, roles y autenticación con BCrypt |
| catalogo-service | 8082 | bibliogo_catalogo | Catálogo de libros, stock y disponibilidad |
| carrito | 8083 | bibliogo_carrito | Carrito de reserva de libros |
| prestamo | 8084 | bibliogo_prestamos | Préstamos de libros con fechas automáticas |
| envio | 8085 | bibliogo_envios | Gestión de envíos y entregas |
| pago | 8086 | bibliogo_pagos | Pagos y multas |
| notificaciones | 8087 | bibliogo_notificaciones | Notificaciones a usuarios |
| resena | 8088 | bibliogo_resenas | Reseñas y calificaciones de libros |
| reporte | 8089 | bibliogo_reportes | Reportes administrativos |

## Funcionalidades implementadas
- CRUD completo en todos los microservicios
- Patrón CSR con separación de responsabilidades
- DTOs para separar entidades de datos de entrada/salida
- Validaciones con Bean Validation (@NotBlank, @NotNull, @Email, @Min, @Max)
- Manejo de excepciones centralizado con @RestControllerAdvice
- Códigos HTTP correctos (200, 201, 400, 500)
- Encriptación de contraseñas con BCrypt
- Comunicación entre microservicios con WebClient
- Reglas de negocio (stock automático, disponibilidad, fechas de préstamo)
- DataLoader con datos iniciales en cada microservicio

## Comunicación entre microservicios
- **prestamo → catalogo**: verifica que el libro existe y tiene stock antes de crear un préstamo
- **prestamo → usuarios**: verifica que el usuario existe antes de crear un préstamo
- **carrito → catalogo**: verifica que el libro está disponible antes de agregar al carrito

## Reglas de negocio principales
- El stock se reduce automáticamente al crear un préstamo
- La disponibilidad cambia a "no disponible" cuando el stock llega a 0
- Las contraseñas se encriptan con BCrypt antes de guardarse
- La fecha de devolución se asigna automáticamente (7 días después del préstamo)
- Si se devuelve tarde, el estado cambia a "devuelto con retraso"
- Las reseñas tienen calificación entre 1 y 5

## Pasos para ejecutar el proyecto

### Requisitos previos
- Java 21
- Maven
- MySQL corriendo en localhost:3306

### 1. Crear las bases de datos en MySQL
```sql
CREATE DATABASE bibliogo_usuarios;
CREATE DATABASE bibliogo_catalogo;
CREATE DATABASE bibliogo_carrito;
CREATE DATABASE bibliogo_prestamos;
CREATE DATABASE bibliogo_envios;
CREATE DATABASE bibliogo_pagos;
CREATE DATABASE bibliogo_notificaciones;
CREATE DATABASE bibliogo_resenas;
CREATE DATABASE bibliogo_reportes;
```

### 2. Ejecutar cada microservicio
Abrir una terminal por cada microservicio y ejecutar:

```bash
# Usuarios
cd UsuariosMicro
mvn spring-boot:run

# Catálogo
cd catalogo-service
mvn spring-boot:run

# Carrito
cd carrito/carrito
mvn spring-boot:run

# Préstamos
cd prestamo
mvn spring-boot:run

# Envíos
cd envio
mvn spring-boot:run

# Pagos
cd pago/pago
mvn spring-boot:run

# Notificaciones
cd notificaciones
mvn spring-boot:run

# Reseñas
cd resena
mvn spring-boot:run

# Reportes
cd reporte
mvn spring-boot:run
```

### 3. Probar con Postman
Importar colección de Postman y probar los endpoints de cada microservicio.

## Repositorio
https://github.com/simonpanita18-cell/primer-proyecto
