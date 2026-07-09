<div align="center">

<img src="docs/images/banner.png" alt="BiblioGo Banner" width="100%"/>

# 📚 BiblioGo

### Sistema de Gestión Bibliotecaria basado en Arquitectura de Microservicios

*Automatización integral de préstamos, catálogo, pagos, envíos, notificaciones y reportes mediante 9 microservicios desacoplados*

<br/>

**Examen Transversal — Ingeniería de Software · Duoc UC**

<br/>

[![Java](https://img.shields.io/badge/Java-21-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)](https://www.oracle.com/java/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.4.1-6DB33F?style=for-the-badge&logo=springboot&logoColor=white)](https://spring.io/projects/spring-boot)
[![Spring Cloud](https://img.shields.io/badge/Spring%20Cloud-Eureka%20%7C%20Gateway-6DB33F?style=for-the-badge&logo=spring&logoColor=white)](https://spring.io/projects/spring-cloud)
[![Docker](https://img.shields.io/badge/Docker-Compose-2496ED?style=for-the-badge&logo=docker&logoColor=white)](https://www.docker.com/)
[![MySQL](https://img.shields.io/badge/MySQL-8.4-4479A1?style=for-the-badge&logo=mysql&logoColor=white)](https://www.mysql.com/)
[![Maven](https://img.shields.io/badge/Maven-3.8+-C71A36?style=for-the-badge&logo=apachemaven&logoColor=white)](https://maven.apache.org/)
[![JWT](https://img.shields.io/badge/Auth-JWT-000000?style=for-the-badge&logo=jsonwebtokens&logoColor=white)](https://jwt.io/)
[![Estado](https://img.shields.io/badge/Estado-Finalizado-success?style=for-the-badge)](#-estado-del-proyecto)
[![Licencia](https://img.shields.io/badge/Licencia-Académica-lightgrey?style=for-the-badge)](#-licencia)

<br/>

[📖 Descripción](#-descripción) •
[🚀 Quick Start](#-quick-start) •
[🏗 Arquitectura](#-arquitectura) •
[🐳 Docker](#-ejecución-con-docker) •
[📚 API](#-documentación-api)

</div>

---

## 📑 Tabla de Contenidos

- [📖 Descripción](#-descripción)
- [🎯 Objetivos del proyecto](#-objetivos-del-proyecto)
- [✨ Características principales](#-características-principales)
- [🏗 Arquitectura](#-arquitectura)
- [🔄 Flujo de una solicitud](#-flujo-de-una-solicitud)
- [⚙ Tecnologías utilizadas](#-tecnologías-utilizadas)
- [📦 Infraestructura y Microservicios](#-infraestructura-y-microservicios)
- [📁 Organización del proyecto](#-organización-del-proyecto)
- [📋 Requisitos previos](#-requisitos-previos)
- [🚀 Quick Start](#-quick-start)
- [💻 Instalación](#-instalación)
- [⚙ Configuración](#-configuración)
- [🐳 Ejecución con Docker](#-ejecución-con-docker)
- [▶ Ejecución Local](#-ejecución-local)
- [🔍 Verificación](#-verificación)
- [📚 Documentación API](#-documentación-api)
- [🔐 Autenticación](#-autenticación)
- [🧪 Pruebas](#-pruebas)
- [❗ Problemas comunes](#-problemas-comunes)
- [📈 Estado del Proyecto](#-estado-del-proyecto)
- [🚀 Mejoras futuras](#-mejoras-futuras)
- [👨‍💻 Autor](#-autor)
- [📄 Licencia](#-licencia)

---

## 📖 Descripción

**BiblioGo** es un sistema de gestión bibliotecaria de alta disponibilidad, construido bajo una **arquitectura de microservicios**, enfocado en automatizar los procesos operativos de una biblioteca: gestión de usuarios, catálogo de libros con control de stock, préstamos, pagos de multas, logística de envíos, notificaciones, reseñas y reportes administrativos.

El sistema está compuesto por **nueve microservicios independientes**, cada uno con su propia responsabilidad de negocio, que se registran dinámicamente en un **Eureka Server** y son orquestados a través de un **API Gateway** como punto único de entrada. La seguridad se gestiona mediante **autenticación JWT**, y la comunicación entre servicios se realiza vía REST con **Spring WebClient**.

El proyecto fue desarrollado como parte del **Examen Transversal de la asignatura de Ingeniería de Software de Duoc UC**, con el código full-stack construido en el ramo de **Full Stack 1** (profesor Cristian Vega) y complementado con la documentación de arquitectura y calidad de Ingeniería de Software. La estructura y documentación del repositorio siguen las convenciones de un proyecto open source profesional, de modo que cualquier persona pueda clonarlo, levantarlo con Docker y comenzar a interactuar con la API sin asistencia adicional.

---

## 🎯 Objetivos del proyecto

| # | Objetivo | Descripción |
|:-:|---|---|
| 1 | **Automatizar la gestión bibliotecaria** | Eliminar procesos manuales de registro de préstamos, devoluciones y control de stock. |
| 2 | **Optimizar préstamos y devoluciones** | Calcular fechas límite automáticamente y bloquear préstamos sin stock disponible. |
| 3 | **Mejorar la experiencia de usuario y administrador** | Flujos claros de catálogo, préstamos, envíos y reportes. |
| 4 | **Implementar una arquitectura desacoplada** | 9 microservicios independientes, cada uno con su propia responsabilidad y base de datos. |
| 5 | **Garantizar escalabilidad y mantenibilidad** | Service Discovery (Eureka) + API Gateway centralizado. |
| 6 | **Facilitar futuras ampliaciones** | Bajo acoplamiento para incorporar nuevos módulos sin afectar servicios existentes. |
| 7 | **Aplicar estándares de calidad de software** | ISO/IEC 25010, modelado UML (4+1) y prototipado en Figma. |

---

## ✨ Características principales

### 👤 Rol Usuario
- 🔐 Registro e inicio de sesión seguro (contraseñas con hash **BCrypt**).
- 📖 Consulta del catálogo con stock en tiempo real.
- 🛒 Carrito de solicitudes antes de confirmar un préstamo.
- 📦 Seguimiento del estado del envío y del historial de devoluciones.
- ⭐ Reseñas y calificaciones (1–5) sobre libros leídos.
- 🔔 Notificaciones automáticas de vencimientos y devoluciones.

### 🛠 Rol Administrador
- 👥 Gestión integral de usuarios y catálogo.
- ⏰ Penalizaciones automáticas por atraso.
- 📊 Reportes administrativos exportables.
- 🚚 Gestión logística de envíos físicos.

### ⚙ Técnicas
- 🧩 9 microservicios + Eureka Server + API Gateway.
- 🔎 Service Discovery dinámico, sin URLs hardcodeadas.
- 🗄 MySQL 8.4 dockerizado.
- 🐳 Despliegue reproducible con Docker Compose y *health checks*.
- 🧪 Pruebas unitarias con JUnit y Mockito.

---

## 🏗 Arquitectura

```
                        ┌─────────────────────┐
                        │   Eureka Server      │
                        │   (puerto 8761)       │
                        └───────────▲───────────┘
                                    │ registro
Cliente ──▶ API Gateway ───────────┼──────────────────────────────┐
            (puerto 8090)          │                              │
                    │              ▼                              ▼
      ┌─────────────┼───────────────────────────────────────────────────┐
      ▼             ▼             ▼             ▼             ▼         ▼
  Usuarios      Catálogo      Carrito       Préstamo        Pago    Notificaciones
  (8081)         (8082)        (8083)        (8084)        (8085)      (8086)
                                                                          │
                                                        ┌─────────────────┼───────────────┐
                                                        ▼                 ▼                ▼
                                                     Envío           Reseña          Reporte
                                                     (8087)          (8088)          (8089)
                                                        │                 │                │
                                                        └────────────┬────┴────────────────┘
                                                                     ▼
                                                              MySQL (puerto 3307)
```

- 🧭 **Eureka Server** — todos los servicios se registran ahí al arrancar.
- 🚪 **API Gateway** — único punto de entrada; enruta y filtra seguridad (JWT).
- 🔗 **WebClient** — comunicación REST entre microservicios (ej. Préstamo ↔ Catálogo).
- 🗄 **MySQL dockerizado** — esquema `bibliogo_usuarios` como base principal.

---

## 🔄 Flujo de una solicitud

Ejemplo: un usuario confirma un préstamo.

1. **Cliente** envía `POST /api/prestamo/confirmar` al **Gateway** (`localhost:8090`), con su JWT en el header.
2. El **Gateway** valida el token y consulta a **Eureka** la ubicación de `prestamo-service`.
3. `prestamo-service` llama vía **WebClient** a `catalogo-service` para verificar stock.
4. Si hay stock, `catalogo-service` **reduce el inventario** y responde OK.
5. `prestamo-service` **crea el préstamo**, calcula la fecha de devolución y persiste en MySQL.
6. `prestamo-service` notifica de forma asíncrona a `notificaciones-service`.
7. La respuesta viaja de vuelta: `prestamo-service → Gateway → Cliente`.

---

## ⚙ Tecnologías utilizadas

| Categoría | Tecnología |
|---|---|
| Lenguaje | Java 21 |
| Framework | Spring Boot 3.4.1 |
| Ecosistema distribuido | Spring Cloud (Eureka, Gateway) |
| Comunicación entre servicios | Spring WebClient |
| Base de datos | MySQL 8.4 (dockerizado) |
| Build tool | Maven 3.8+ |
| Seguridad | JWT + BCrypt |
| Contenedores | Docker & Docker Compose |
| Documentación de API | Swagger / OpenAPI |
| Testing | JUnit + Mockito |
| Control de versiones | Git / GitHub |
| Diseño y gestión | Figma, Trello |

---

## 📦 Infraestructura y Microservicios

| Servicio | Puerto | Responsabilidad |
|---|:---:|---|
| `mysql-bibliogo` | `3307` | Base de datos relacional (esquema principal: `bibliogo_usuarios`) |
| `eureka-server` | `8761` | Servidor de descubrimiento de instancias (Service Discovery) |
| `api-gateway` | `8090` | Gateway principal, enrutador central y filtro de seguridad |
| `usuarios-micro` | `8081` | Gestión de usuarios, roles y generación de JWT |
| `catalogo-service` | `8082` | Administración de libros, autores, categorías y stock |
| `carrito-service` | `8083` | Persistencia temporal de reservas antes del préstamo |
| `prestamo-service` | `8084` | Orquestador del ciclo de vida de préstamos y devoluciones |
| `pago-service` | `8085` | Simulación y registro de comprobantes de pago de multas |
| `notificaciones-service` | `8086` | Despacho interno de alertas y avisos |
| `envio-service` | `8087` | Logística, despacho físico y tracking de entrega |
| `resena-service` | `8088` | Sistema de feedback con comentarios y valoraciones |
| `reporte-service` | `8089` | Motor analítico de métricas de libros y usuarios activos |

---

## 📁 Organización del proyecto

```
BiblioGo/
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
```

---

## 📋 Requisitos previos

Antes de levantar el proyecto, asegúrate de tener instalado:

| Herramienta | Versión | Verificar con |
|---|---|---|
| Java (JDK) | 21 | `java -version` |
| Maven | 3.8+ | `mvn -version` |
| Docker Desktop | Última | `docker -v` |
| Git | Última | `git --version` |

> 🪟 Todos los comandos de esta guía están escritos para **PowerShell** (Windows). Si usas macOS/Linux, la sintaxis de los comandos `mvn`, `docker` y `git` es idéntica.

---

## 🚀 Quick Start

La forma más rápida de levantar todo el sistema (backend + base de datos) usando Docker:

```powershell
# 1. Clonar el repositorio
git clone https://github.com/simon/primer-proyecto.git
cd primer-proyecto

# 2. Compilar todos los microservicios (genera los .jar)
$services = @("eureka-server","api-gateway","UsuariosMicro","catalogo-service","carrito","prestamo","pago","notificaciones","envio","resena","reporte")
foreach ($s in $services) { mvn clean package -DskipTests -f "$s/pom.xml" }

# 3. Construir las imágenes Docker
docker compose build

# 4. Levantar todo el sistema
docker compose up -d

# 5. Verificar que todo esté arriba
docker compose ps
```

Espera unos **30 segundos** tras el arranque para que todos los servicios terminen de registrarse en Eureka. Luego entra a `http://localhost:8761` para confirmarlo.

---

## 💻 Instalación

Paso a paso detallado (equivalente al Quick Start, pero explicado):

**1. Clona el repositorio:**
```powershell
git clone https://github.com/simon/primer-proyecto.git
cd primer-proyecto
```

**2. Compila cada microservicio.** Esto genera el archivo `.jar` que luego usará cada `Dockerfile`:
```powershell
mvn clean package -DskipTests -f eureka-server/pom.xml
mvn clean package -DskipTests -f api-gateway/pom.xml
mvn clean package -DskipTests -f UsuariosMicro/pom.xml
mvn clean package -DskipTests -f catalogo-service/pom.xml
mvn clean package -DskipTests -f carrito/pom.xml
mvn clean package -DskipTests -f prestamo/pom.xml
mvn clean package -DskipTests -f pago/pom.xml
mvn clean package -DskipTests -f notificaciones/pom.xml
mvn clean package -DskipTests -f envio/pom.xml
mvn clean package -DskipTests -f resena/pom.xml
mvn clean package -DskipTests -f reporte/pom.xml
```
> `-DskipTests` omite las pruebas unitarias para acelerar el build local. Quítalo si quieres que Maven las ejecute.

**3. Verifica que cada carpeta tenga su `.jar` generado** dentro de `target/` (ej. `eureka-server/target/eureka-server-0.0.1-SNAPSHOT.jar`).

Con esto, el proyecto ya está listo para pasar a **Configuración** o directamente a **Docker**.

---

## ⚙ Configuración

### 🗄 Base de datos
El contenedor de MySQL crea el esquema automáticamente al iniciar, usando `init-db.sql`. No necesitas crear tablas manualmente.

| Parámetro | Valor por defecto |
|---|---|
| Host | `localhost` |
| Puerto (externo) | `3307` |
| Puerto (interno, entre contenedores) | `3306` |
| Usuario | `root` |
| Contraseña | `rootpass` |
| Esquema principal | `bibliogo_usuarios` |

### 🔑 Variables de entorno
Definidas en `docker-compose.yml` (no requieren `.env` adicional para el entorno por defecto):

```yaml
MYSQL_ROOT_PASSWORD: rootpass
MYSQL_DATABASE: bibliogo_usuarios
SPRING_DATASOURCE_URL: jdbc:mysql://mysql-bibliogo:3306/bibliogo_usuarios
EUREKA_CLIENT_SERVICEURL_DEFAULTZONE: http://eureka-server:8761/eureka/
```

### 🔐 Configuración JWT
Cada microservicio protegido valida el token emitido por `usuarios-micro`. La clave secreta y el tiempo de expiración se definen en el `application.yml` de cada servicio:

```yaml
jwt:
  secret: bibliogo_secret_key
  expiration: 3600000 # 1 hora en milisegundos
```

### 🐳 Configuración de Docker
Cada microservicio tiene su propio `Dockerfile`, y `docker-compose.yml` orquesta toda la red interna, exponiendo únicamente los puertos necesarios hacia el host (ver tabla de [Infraestructura](#-infraestructura-y-microservicios)).

---

## 🐳 Ejecución con Docker

### 1️⃣ Build — construir las imágenes
```powershell
docker compose build
```
Esto lee cada `Dockerfile`, copia el `.jar` ya compilado de cada microservicio y arma la imagen correspondiente.

### 2️⃣ Up — levantar los contenedores
```powershell
docker compose up -d
```
El flag `-d` corre todo en segundo plano (*detached*). **¿Qué ocurre internamente?**
Gracias a las directivas `depends_on` con `condition: service_healthy`, Docker Compose respeta este orden:
1. **`mysql-bibliogo`** arranca primero y espera a pasar su *health check* (`mysqladmin ping`).
2. **`eureka-server`** se levanta apenas MySQL está saludable.
3. **`api-gateway`** y **los 9 microservicios** arrancan en paralelo una vez que Eureka está disponible, y se registran automáticamente en él.

### 3️⃣ Logs — ver qué está pasando
Todos los servicios a la vez:
```powershell
docker compose logs -f
```
Un servicio específico (ejemplo, usuarios):
```powershell
docker logs -f usuarios-micro
```
`-f` mantiene el stream abierto (*follow*), útil para depurar en vivo.

### 4️⃣ Ps — ver estado de los contenedores
```powershell
docker compose ps
```
Debes ver los 12 contenedores (MySQL + Eureka + Gateway + 9 microservicios) con estado `Up` o `healthy`.

### 5️⃣ Down — detener y limpiar
```powershell
docker compose down
```
Detiene y elimina los contenedores y la red interna (los datos de MySQL persisten si usas un volumen; si quieres borrarlos también, usa `docker compose down -v`).

---

## ▶ Ejecución Local

Si prefieres correr los servicios sin Docker (ej. desde tu IDE), respeta este **orden estricto**:

1. **MySQL** — debe estar disponible primero; todos los servicios lo necesitan para levantar su contexto de JPA.
2. **`eureka-server`** — segundo, porque el resto de servicios necesita un Eureka activo para registrarse al arrancar.
3. **`api-gateway`** — tercero, ya que enruta hacia servicios que Eureka aún no conoce si arranca antes.
4. **Resto de microservicios** (usuarios, catálogo, carrito, préstamo, pago, notificaciones, envío, reseña, reporte) — en cualquier orden entre sí, una vez que Eureka y la base de datos están arriba.

> ⚠️ Si un microservicio arranca antes que Eureka, fallará su registro inicial y quedará invisible para el Gateway hasta el siguiente ciclo de heartbeat (~30s) o hasta reiniciarlo.

---

## 🔍 Verificación

### Eureka
Abre en el navegador:
```
http://localhost:8761
```
Todos los microservicios deben figurar listados en verde con estado **UP**.

### Swagger (por servicio)

| Servicio | Puerto | URL |
|---|:---:|---|
| usuarios-micro | 8081 | `http://localhost:8081/swagger-ui/index.html` |
| catalogo-service | 8082 | `http://localhost:8082/swagger-ui/index.html` |
| carrito-service | 8083 | `http://localhost:8083/swagger-ui/index.html` |
| prestamo-service | 8084 | `http://localhost:8084/swagger-ui/index.html` |
| pago-service | 8085 | `http://localhost:8085/swagger-ui/index.html` |
| notificaciones-service | 8086 | `http://localhost:8086/swagger-ui/index.html` |
| envio-service | 8087 | `http://localhost:8087/swagger-ui/index.html` |
| resena-service | 8088 | `http://localhost:8088/swagger-ui/index.html` |
| reporte-service | 8089 | `http://localhost:8089/swagger-ui/index.html` |

### Base de datos
Acceder al contenedor de MySQL:
```powershell
docker exec -it mysql-bibliogo mysql -u root -p
```
Contraseña por defecto: `rootpass`

Comandos de verificación una vez dentro:
```sql
SHOW DATABASES;
USE bibliogo_usuarios;
SHOW TABLES;
SELECT * FROM usuarios;
```

### Docker
```powershell
docker compose ps
```
Confirma que los 12 contenedores estén `Up` o `healthy`.

---

## 📚 Documentación API

Todos los endpoints se consumen a través del **API Gateway** en `http://localhost:8090`.

### Registrar usuario
```http
POST http://localhost:8090/api/auth/register
Content-Type: application/json
```
```json
{
  "nombre": "Simón",
  "apellido": "Hércules",
  "email": "simon@test.cl",
  "password": "mi_password_segura",
  "rol": "USER"
}
```
**Respuesta esperada (201 Created):**
```json
{
  "id": 1,
  "nombre": "Simón",
  "email": "simon@test.cl",
  "rol": "USER"
}
```

### Iniciar sesión
```http
POST http://localhost:8090/api/auth/login
Content-Type: application/json
```
```json
{
  "email": "simon@test.cl",
  "password": "mi_password_segura"
}
```
**Respuesta esperada (200 OK):**
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "tipo": "Bearer"
}
```

### Confirmar préstamo (endpoint protegido)
```http
POST http://localhost:8090/api/prestamo/confirmar
Content-Type: application/json
Authorization: Bearer <token-obtenido-en-login>
```
```json
{
  "usuarioId": 1,
  "metodoEnvio": "Domicilio",
  "direccionEnvio": "Av. Apoquindo 4500, Las Condes",
  "metodoPago": "Débito"
}
```
**Respuesta esperada (200 OK):**
```json
{
  "prestamoId": 15,
  "estado": "CONFIRMADO",
  "fechaLimiteDevolucion": "2026-07-23"
}
```

> 📖 Cada microservicio expone su propio Swagger con **todos** sus endpoints documentados (ver tabla en [Verificación](#-verificación)). Los tres ejemplos anteriores cubren el flujo principal de autenticación + préstamo.

---

## 🔐 Autenticación

El sistema usa **JWT (JSON Web Token)** para proteger los endpoints. Flujo completo:

1. **Registro** — `POST /api/auth/register` crea el usuario con contraseña hasheada en **BCrypt**.
2. **Login** — `POST /api/auth/login` valida las credenciales y devuelve un **token JWT**.
3. **Obtención del JWT** — el token viene en el campo `token` de la respuesta del login. Cópialo completo (sin comillas).
4. **Uso del JWT** — en cada request a un endpoint protegido, agrega el header:
   ```
   Authorization: Bearer <tu_token_aquí>
   ```
   Sin este header (o con un token vencido/inválido), el Gateway responde `401 Unauthorized` o `403 Forbidden`.

---

## 🧪 Pruebas

Cómo probar el sistema completo desde Swagger, sin Postman:

1. Abre el Swagger de `usuarios-micro`: `http://localhost:8081/swagger-ui/index.html`.
2. Ejecuta `POST /api/auth/register` con **Try it out** para crear tu usuario.
3. Ejecuta `POST /api/auth/login` y copia el `token` de la respuesta.
4. Haz clic en el botón **Authorize** (🔒, arriba a la derecha en Swagger) y pega: `Bearer <tu_token>`.
5. Ve al Swagger del servicio que quieras probar (ej. `catalogo-service` en el puerto 8082) y ejecuta sus endpoints con **Try it out** — ya quedarás autenticado en esa sesión de Swagger.
6. Repite el paso de **Authorize** en cada Swagger distinto, ya que cada uno corre en un puerto/instancia diferente.

---

## ❗ Problemas comunes

| Error | Causa | Solución |
|---|---|---|
| `503 Service Unavailable` | El Gateway aún no localizó el servicio en Eureka. | Espera ~30 segundos tras el arranque y reintenta. |
| `401 Unauthorized` / `403 Forbidden` | El token JWT falta, expiró, o no lleva la palabra `Bearer`. | Vuelve a hacer login y revisa el header `Authorization: Bearer <token>`. |
| Conflicto en el puerto MySQL (3306/3307) | Tienes una instancia local de MySQL corriendo en tu máquina. | Detén el servicio de MySQL nativo en Windows antes de levantar Docker. |
| Un microservicio no aparece en Eureka | Arrancó antes que `eureka-server`. | Reinícialo con `docker compose restart <servicio>` o respeta el orden de [Ejecución Local](#-ejecución-local). |

---

## 📈 Estado del Proyecto

✅ **Finalizado** — ecosistema funcional e integrado, desarrollado con Spring Boot, Spring Cloud y Docker, entregado como Examen Transversal de Ingeniería de Software.

---

## 🚀 Mejoras futuras

- 💳 Integración con una pasarela de pago real para el pago de multas.
- 📱 Cliente frontend (web o móvil) consumiendo la API pública.
- 📅 Sistema de reservas anticipadas de libros.
- 🔁 Pipeline CI/CD para build y despliegue automático de imágenes.
- 📊 Dashboard administrativo con métricas en tiempo real (basado en `reporte-service`).
- 🧪 Ampliar cobertura de pruebas unitarias e incorporar pruebas de integración.

---

## 👨‍💻 Autor

**Simón Eduardo Hércules Márquez**

- 🎓 **Carrera:** Analista Programador
- 🏫 **Institución:** Duoc UC
- 💻 **Código Full-Stack:** desarrollado en la asignatura **Full Stack 1**, profesor **Cristian Vega**.
- 📄 **Documentación técnica:** elaborada para el Examen Transversal de **Ingeniería de Software**, docente **Osnellys Andrade**.

---

## 📄 Licencia

Proyecto desarrollado con fines **académicos** para el Examen Transversal de Ingeniería de Software de Duoc UC.
All rights reserved © 2026.