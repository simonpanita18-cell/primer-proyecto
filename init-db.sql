CREATE DATABASE IF NOT EXISTS bibliogo_usuarios
  CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

CREATE DATABASE IF NOT EXISTS bibliogo_catalogo
  CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

CREATE DATABASE IF NOT EXISTS bibliogo_carrito
  CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

CREATE DATABASE IF NOT EXISTS bibliogo_prestamos
  CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

CREATE DATABASE IF NOT EXISTS bibliogo_pagos
  CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

CREATE DATABASE IF NOT EXISTS bibliogo_notificaciones
  CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

CREATE DATABASE IF NOT EXISTS bibliogo_envios
  CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

CREATE DATABASE IF NOT EXISTS bibliogo_resenas
  CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

CREATE DATABASE IF NOT EXISTS bibliogo_reportes
  CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

GRANT ALL PRIVILEGES ON bibliogo_usuarios.* TO 'root'@'%';
GRANT ALL PRIVILEGES ON bibliogo_catalogo.* TO 'root'@'%';
GRANT ALL PRIVILEGES ON bibliogo_carrito.* TO 'root'@'%';
GRANT ALL PRIVILEGES ON bibliogo_prestamos.* TO 'root'@'%';
GRANT ALL PRIVILEGES ON bibliogo_pagos.* TO 'root'@'%';
GRANT ALL PRIVILEGES ON bibliogo_notificaciones.* TO 'root'@'%';
GRANT ALL PRIVILEGES ON bibliogo_envios.* TO 'root'@'%';
GRANT ALL PRIVILEGES ON bibliogo_resenas.* TO 'root'@'%';
GRANT ALL PRIVILEGES ON bibliogo_reportes.* TO 'root'@'%';

FLUSH PRIVILEGES;