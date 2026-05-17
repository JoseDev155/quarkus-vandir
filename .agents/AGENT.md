# AGENT.md - Vandir System Configuration & Code Style Guidelines

Este archivo define las reglas de contexto, convenciones de código y decisiones de arquitectura definitivas para el desarrollo del ecosistema del sistema **Vandir**. El Agente de IA debe leer, comprender y aplicar estas directrices de manera estricta en cada generación, refactorización o corrección de código.

---

## 1. Contexto Global del Sistema
* **Propósito:** Sistema transaccional moderno y robusto con gestión de inventario, punto de venta y control de acceso basado en tres roles definidos: `Administrador`, `Gerente` y `Vendedor`.
* **Enfoque de Seguridad:** Arquitectura sin estado (Stateless) basada en tokens JWT para la comunicación entre el Frontend y el Backend.

---

## 2. Convenciones del Backend (Java & Quarkus)

### Entorno Tecnológico
* **Lenguaje:** Java 25 LTS (Versión moderna/actual).
* **Framework:** Quarkus (Versión actual/estable bajo la especificación Jakarta EE).
* **Persistencia:** Hibernate ORM con Panache utilizando el patrón **Repository**.
* **API:** REST basada en Quarkus REST (Jakarta REST).

### Convenciones de Nomenclatura (Java)
* **Clases e Interfaces:** `UpperCamelCase` (ej. `ProductoService`, `UsuarioRepository`).
* **Variables y Métodos:** `lowerCamelCase` (ej. `precioUnitario`, `calcularTotalVenta()`).
* **Constantes:** `UPPER_SNAKE_CASE` (ej. `MAX_LOGIN_ATTEMPTS`, `JWT_EXPIRATION_TIME`).
* **Estructuras de Datos:** **Prohibido el uso de Java Records.** Toda entidad o DTO debe implementarse utilizando clases tradicionales de Java.

### Arquitectura Backend: MVC Extendido
El backend se estructurará estrictamente dividiendo las responsabilidades en cuatro capas:

1.  **Model (Entidades):** Clases Java tradicionales mapeadas a la base de datos mediante anotaciones de `jakarta.persistence`. No extienden de `PanacheEntity` directamente; contienen sus atributos y propiedades mapeadas de forma explícita.
2.  **Repository:** Clases que implementan `PanacheRepository<T>`. Centralizan todas las consultas a la base de datos utilizando Hibernate con Panache, abstrayendo la capa de datos.
3.  **Services:** Capa intermedia donde reside exclusivamente la lógica de negocio, validaciones transaccionales y coordinación de llamadas a los repositorios.
4.  **Controllers (Resources):** Clases anotadas con `@Path` que exponen los endpoints de la API REST. Inyectan los servicios correspondientes y gestionan las peticiones/respuestas HTTP, mapeando los formatos JSON.

### Convenciones Específicas de Quarkus
* **Inyección de Dependencias:** Utilizar la especificación CDI actual con `@Inject` o preferiblemente inyección por constructor para facilitar las pruebas unitarias.
* **Seguridad:** Uso de `@RolesAllowed` directamente en los métodos del Controller/Resource, integrando `quarkus-security-jpa` y `quarkus-smallrye-jwt`.
* **Manejo de Respuestas:** Retornar siempre objetos `jakarta.ws.rs.core.Response` para tener control total sobre los códigos de estado HTTP (200, 201, 401, 403, 500) y los cuerpos de respuesta.

---

## 3. Convenciones del Frontend (JavaScript & Vue)

### Entorno Tecnológico
* **Runtime:** Node.js v24.14.1.
* **Gestor de Paquetes:** `pnpm`.
* **Framework:** Vue 3 (Versión moderna/actual).
* **Estándar de JavaScript:** ECMAScript 2025 (ES2025) nativo y moderno (Uso de async/await, encadenamiento opcional, asignación lógica y métodos modernos de arrays).

### Convenciones de Nomenclatura (JavaScript)
* **Archivos de Componentes Vue:** `UpperCamelCase` (ej. `PuntoVenta.vue`, `MenuLateral.vue`).
* **Variables, Funciones y Propiedades:** `lowerCamelCase` (ej. `carritoCompras`, `actualizarStock()`).
* **Módulos o Clases JS auxiliares:** `UpperCamelCase` (ej. `AuthService.js`).

### Convenciones Específicas de Vue
* **Paradigma:** Uso estricto de la **Composition API** mediante la sintaxis abreviada `<script setup>`.
* **Manejo de Estado:** Uso de reactividad nativa con `ref()` y `reactive()`.
* **Estilos:** Integración de la hoja de estilos maestra global proporcionada (`style.css`). Evitar estilos en línea; reutilizar rigurosamente las variables de CSS (`var(--primary-color)`) y las clases utilitarias del documento maestro.
* **Enrutamiento:** Utilizar Vue Router de forma declarativa, implementando *Navigation Guards* (`router.beforeEach`) para interceptar el token JWT guardado en el cliente y restringir el acceso a las vistas según el rol del usuario.

---

## 4. Instrucciones de Generación de Código para el Agente
* **Precisión de Contexto:** Al generar código para el Frontend, asume que el backend responderá con objetos JSON estructurados exactamente igual que las tablas definidas en el script de base de datos original.
* **Seguridad Obligatoria:** Cada endpoint del backend orientado a roles específicos debe contar con la anotación de seguridad correspondiente. Cada formulario del frontend debe incluir el token JWT en las cabeceras HTTP de la petición (`Authorization: Bearer <token>`).
* **Código Limpio:** No generes comentarios excesivos ni código *boilerplate* redundante. Prioriza la legibilidad del JavaScript moderno y las facilidades sintácticas de Quarkus Panache.