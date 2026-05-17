# Lógica del Backend (Quarkus)

* Usa Java 25 como base para el código generado. No utilices características exclusivas de Java 21+ a menos que se solicite.
* Ejecuta los comandos siempre utilizando el Maven Wrapper local (`./mvnw` o `.\mvnw`). No asumas que Maven está instalado globalmente.
* La base de datos es MySQL (`vandir_db`). El script inicial (`script_db.sql`) ya define las tablas, ENUMs y relaciones. Respétalas al 100%.
* Utiliza el patrón Active Record/Repository simplificado de Panache (ej. `Producto.list("stock_actual < stock_minimo")`). Evita escribir consultas SQL nativas o JPQL extensas a menos que sea una consulta multitable compleja.
* Retorna siempre DTOs (Data Transfer Objects) o las entidades serializadas a JSON. No incluyas lógica de presentación en el backend.

# Lógica del Frontend (Vue 3)

* Para archivos JavaScript auxiliares o configuraciones de Vite/Router, usa `camelCase`.
* En el caso de componentes y vistas (`.vue`), usa estrictamente `PascalCase`.
* Estructura de nombres en la carpeta `src/`:
  * `components/`: `BotonPrimario.vue`, `TablaDatos.vue`, `TarjetaEstadistica.vue` (Piezas atómicas y reutilizables).
  * `views/`: `Login.vue`, `PanelGerente.vue`, `PuntoVenta.vue` (Vistas completas enrutadas).
  * `router/`: `index.js` (Manejo de rutas y Navigation Guards para el JWT).
* Usa `fetch()` nativo nativo con la sintaxis moderna de ES2025.
* Implementa una función de utilidad global (ej. `apiClient.js`) para inyectar automáticamente el JWT del `localStorage` en los headers y manejar respuestas no autorizadas (401).

# Lógica por implementar (Core: Punto de Venta)

* **Frontend:** El Vendedor selecciona productos de la tabla principal y se agregan a un arreglo reactivo (carrito).
* **Frontend:** Al presionar "Cobrar", se construye un JSON con el ID del vendedor (obtenido del JWT) y el arreglo de productos (ID y cantidad).
* **Backend:** El endpoint `/api/ventas` recibe el JSON. Inicia una transacción (`@Transactional`).
* **Backend (Validación):** Verifica en tiempo real con Panache si hay stock suficiente en la tabla `productos` antes de proceder.
* **Backend (Operación):** Registra la cabecera en `ventas`, inserta los items en `detalle_ventas` y descuenta el stock de `productos`.
* **Frontend:** Recibe la confirmación (código HTTP 201), vacía el carrito reactivo y muestra un mensaje de éxito sin recargar la página.

# Estilos y UI

* Uso **estricto y exclusivo** del archivo `src/assets/style.css`.
* Está completamente **prohibido** usar CSS en línea (`style="..."`) dentro de los componentes `<template>` de Vue.
* Utiliza las variables CSS globales ya definidas (ej. `var(--primary-color)`, `var(--danger)`).
* Si un componente necesita estilos específicos, añádelos al final del archivo `style.css` maestro manteniendo la coherencia, o usa un `<style scoped>` muy mínimo si es estructural.

# Prioridades y Trabajo en Equipo

* Escribe código limpio y altamente legible, asumiendo que será revisado y mantenido por los otros 3 estudiantes desarrolladores del equipo.
* Crea componentes aislados para elementos repetitivos (como las tarjetas del dashboard o los inputs del formulario).
* Limita las dependencias externas (`npm install`). Resuelve la lógica de UI nativamente con Vue y CSS antes de sugerir instalar una librería de terceros.
* No modifiques archivos de configuración (como `vite.config.js` o `application.properties`) sin justificar el cambio primero.