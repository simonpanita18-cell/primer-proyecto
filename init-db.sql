CREATE DATABASE IF NOT EXISTS bd_usuarios
  CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

CREATE DATABASE IF NOT EXISTS bd_catalogo
  CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

CREATE DATABASE IF NOT EXISTS bd_carrito
  CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

CREATE DATABASE IF NOT EXISTS bd_prestamos
  CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

CREATE DATABASE IF NOT EXISTS bd_pagos
  CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

CREATE DATABASE IF NOT EXISTS bd_notificaciones
  CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

CREATE DATABASE IF NOT EXISTS bd_envios
  CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

CREATE DATABASE IF NOT EXISTS bd_resenas
  CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

CREATE DATABASE IF NOT EXISTS bd_reportes
  CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

GRANT ALL PRIVILEGES ON bd_usuarios.* TO 'root'@'%';
GRANT ALL PRIVILEGES ON bd_catalogo.* TO 'root'@'%';
GRANT ALL PRIVILEGES ON bd_carrito.* TO 'root'@'%';
GRANT ALL PRIVILEGES ON bd_prestamos.* TO 'root'@'%';
GRANT ALL PRIVILEGES ON bd_pagos.* TO 'root'@'%';
GRANT ALL PRIVILEGES ON bd_notificaciones.* TO 'root'@'%';
GRANT ALL PRIVILEGES ON bd_envios.* TO 'root'@'%';
GRANT ALL PRIVILEGES ON bd_resenas.* TO 'root'@'%';
GRANT ALL PRIVILEGES ON bd_reportes.* TO 'root'@'%';

FLUSH PRIVILEGES;