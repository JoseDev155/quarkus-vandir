# Frontend - Vandir Store
# Índice
- [Frontend - Vandir Store](#frontend---vandir-store)
- [Índice](#índice)
  - [Arquitectura del Frontend](#arquitectura-del-frontend)
  - [Tecnologías del Frontend](#tecnologías-del-frontend)
  - [Instrucciones para el Frontend](#instrucciones-para-el-frontend)
    - [Desarrollo](#desarrollo)
      - [Configurar Node.js](#configurar-nodejs)
      - [Para pnpm](#para-pnpm)
  - [Variables de Entorno](#variables-de-entorno)
  - [Librerías de JavaScript para el proyecto](#librerías-de-javascript-para-el-proyecto)

## Arquitectura del Frontend

El frontend es una **SPA (Single Page Application)** construida con **Vue 3** y **Vite**. La estructura separa vistas, componentes, utilidades y rutas:

```plaintext
vue-frontend/
├── public/            # Iconos, favicon y recursos estáticos
├── src/
│   ├── assets/        # Estilos globales e imágenes
│   ├── components/    # Componentes reutilizables
│   ├── router/        # Rutas y guards
│   ├── utils/         # Cliente API y helpers
│   ├── views/         # Pantallas principales (Login, Inventario, Ventas, etc.)
│   ├── App.vue        # Componente raíz
│   └── main.js        # Punto de entrada
├── .env               # Variables de entorno locales
├── index.html         # HTML base de Vite
├── package.json       # Scripts y dependencias
├── vite.config.js     # Configuración de Vite y alías
└── README-vue.md
```

Bloques principales:
* `views/`: pantallas completas del sistema (login, ventas, compras, inventario).
* `components/`: piezas reutilizables de UI (layout, sidebar, topbar).
* `router/`: rutas y proteccion por rol.
* `utils/`: cliente API con JWT y manejo de errores.

## Tecnologías del Frontend

* **Framework:** [Vue 3](https://vuejs.org/)
* **Ruteo:** [Vue Router](https://router.vuejs.org/)
* **Bundler / dev server:** [Vite](https://vite.dev/)
* **Pruebas unitarias:** [Vitest](https://vitest.dev/)
* **Linter:** [ESLint](https://eslint.org/)
* **Gestor de paquetes:** [pnpm](https://pnpm.io/)

## Instrucciones para el Frontend

### Desarrollo

> Crear un archivo `.env` en la raiz de `vue-frontend/` con las variables del proyecto.

#### Configurar Node.js

Usar una versión LTS compatible con Vite. Verificar con:

```bash
node -v
pnpm -v
```

>Usé Node.js v24.14.1 LTS. Se recomienda administrar versiones con FNM:

#### Para pnpm

1. Instalar dependencias:

```sh
pnpm install
```

2. Ejecutar modo desarrollo:

```sh
pnpm dev
```

3. Generar build de produccion:

```sh
pnpm build
```

4. Ejecutar pruebas unitarias:

```sh
pnpm test:unit
```

5. Lint con ESLint:

```sh
pnpm lint
```

## Variables de Entorno

El frontend usa las siguientes variables:

* `VITE_API_BASE_URL`: URL base del backend, por ejemplo `http://localhost:8080/api`.

## Librerías de JavaScript para el proyecto

* `vue`: base de la interfaz de usuario.
* `vue-router`: navegacion entre vistas y rutas protegidas.
* `vite`: servidor de desarrollo y build.
* `vitest`: pruebas unitarias.
* `eslint`: analisis estatico del codigo.
