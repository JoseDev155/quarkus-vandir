# Flujo de Autenticación y Autorización (JWT) - Vandir System

Este documento describe la arquitectura de seguridad implementada entre el frontend (Vue 3) y el backend (Quarkus REST + SmallRye JWT). El sistema es 100% Stateless.

## 1. Inicio de Sesión (Login)
1. **Frontend:** Envía un `POST` a `/api/auth/login` con un payload JSON `{ "email": "...", "password": "..." }`.
2. **Backend:** * Busca al usuario en la base de datos MySQL usando Panache.
   * Verifica la contraseña.
   * Si es exitoso, Quarkus (SmallRye JWT Build) firma un token JWT. En el payload del token se incluye el email del usuario y su rol (claim `groups`).
3. **Respuesta:** El backend retorna HTTP 200 con el token y el rol en formato JSON: `{ "token": "eyJh...", "rol": "Gerente" }`.

## 2. Almacenamiento en el Cliente (Vue 3)
* El frontend guarda el token y el rol exclusivamente en el `localStorage`:
  * `localStorage.setItem('tokenVandir', token)`
  * `localStorage.setItem('rolVandir', rol)`

## 3. Peticiones Autenticadas (Interceptores / Fetch)
* Para cualquier petición a rutas protegidas, el frontend debe inyectar el token en las cabeceras HTTP de esta manera:

```http
Authorization: Bearer <token_del_localstorage>

```

* Si el token expira o es inválido, el backend devolverá un `401 Unauthorized`. El frontend debe capturar este error, limpiar el `localStorage` y redirigir al `/login`.

## 4. Protección de Rutas (Backend y Frontend)

* **Backend (Quarkus):** Los endpoints están protegidos por anotaciones a nivel de método o clase utilizando la especificación estándar: `@RolesAllowed({"Administrador", "Gerente"})`.
* **Frontend (Vue Router):** Se utiliza un *Navigation Guard* (`router.beforeEach`). Antes de renderizar una vista, verifica si el usuario tiene el token y si su rol almacenado coincide con los metadatos requeridos por la ruta.

## 5. Cierre de Sesión (Logout)

* Ocurre 100% en el lado del cliente.
* El frontend elimina `tokenVandir` y `rolVandir` del `localStorage` y redirige programáticamente a la vista de inicio de sesión. No es necesario enviar una petición al backend para invalidar el token.