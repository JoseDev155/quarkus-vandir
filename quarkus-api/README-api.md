# Vandir Backend - API de Quarkus

Este proyecto utiliza [Quarkus](https://quarkus.io/), el framework de [Java](https://adoptium.net/) supersónico y subatómico, diseñado para ofrecer tiempos de arranque rápidos y un bajo consumo de memoria.

## Índice

- [Vandir Backend - API de Quarkus](#vandir-backend---api-de-quarkus)
  - [Índice](#índice)
  - [Arquitectura del Backend](#arquitectura-del-backend)
    - [Descripción de Capas:](#descripción-de-capas)
  - [Tecnologías Utilizadas](#tecnologías-utilizadas)
  - [Instrucciones de Configuración](#instrucciones-de-configuración)
  - [Ejecución en Modo Desarrollo](#ejecución-en-modo-desarrollo)
    - [Opción A: Usando el script personalizado (Recomendado si se tiene varias versiones de Java)](#opción-a-usando-el-script-personalizado-recomendado-si-se-tiene-varias-versiones-de-java)
    - [Opción B: Usando el Maven Wrapper original](#opción-b-usando-el-maven-wrapper-original)
  - [Empaquetado y Ejecución](#empaquetado-y-ejecución)
  - [Guías de Quarkus Relacionadas](#guías-de-quarkus-relacionadas)

## Arquitectura del Backend

El sistema sigue un patrón de **MVC Extendido**, organizado en capas para asegurar el desacoplamiento y la mantenibilidad:

```plaintext
quarkus-api/
├───src/main/java/com/vandirstore/
│   ├───controller/   # Recursos REST (Controladores) - Capa de Entrada
│   ├───dto/          # Objetos de Transferencia de Datos (Request/Response)
│   ├───model/        # Entidades JPA (Mapeo de Base de Datos)
│   │   └───enums/    # Enumeraciones y Conversores de Atributos
│   ├───repository/   # Capa de Acceso a Datos (Hibernate Panache)
│   ├───service/      # Interfaces de Lógica de Negocio
│   │   └───impl/     # Implementaciones de Servicios (Capa Transaccional)
│   └───ApiApplication.java # Configuración Global de OpenAPI/Swagger
├───src/main/resources/
│   ├───application.properties # Configuración principal de Quarkus
│   ├───privateKey.pem         # Llave privada para firma JWT (No incluir en GIT)
│   └───publicKey.pem          # Llave pública para validación JWT (No incluir en GIT)
├───.env.example      # Plantilla de credenciales (Renombrar a .env)
└───build.ps1         # Script para forzar JDK 25 local
```

### Descripción de Capas:
*   **Modelos:** Clases Java tradicionales que representan las tablas de MySQL (`usuarios`, `productos`, etc.).
*   **Repositorios:** Utilizan el patrón Repository de Panache para realizar consultas a la base de datos de forma simplificada.
*   **Servicios:** Aquí reside la lógica de negocio (ej. validación de stock, cálculo de IVA). Se utilizan interfaces para facilitar el testing.
*   **DTOs:** Aseguran que las entidades de la base de datos no se expongan directamente a la API, protegiendo datos sensibles como contraseñas.
*   **Controladores:** Endpoints REST que reciben peticiones y devuelven respuestas JSON, protegidos por roles mediante JWT.

## Tecnologías Utilizadas

* **Lenguaje:** [Java 25](https://adoptium.net/temurin/releases)
* **Framework:** Quarkus 3.35.3
* **Persistencia:** Hibernate ORM con Panache
* **Base de Datos:** MySQL (Desarrollo) / H2 (Pruebas en memoria)
* **Seguridad:** SmallRye JWT (Basado en roles)
* **Documentación:** OpenAPI & Swagger UI

> **Nota:** Descargar el JDK 25 en formato ZIP y configurar el `JAVA_HOME` en las variables de entorno del sistema.

## Instrucciones de Configuración

Antes de iniciar, asegúrarse de configurar el entorno local:

1. **Variables de Entorno:** Copiar el archivo `.env.example` a uno nuevo llamado `.env` en la raíz de la carpeta `quarkus-api/`.
2. **Credenciales:** Editar el archivo `.env` con el usuario y contraseña de la instancia local de MySQL.
3. **Base de Datos:** Ejecutar el script `script_db.sql` (ubicado en `.agents/context/`) en MySQL para crear las tablas y los datos iniciales.

## Ejecución en Modo Desarrollo

Existen dos formas de levantar la API dependiendo de tu configuración de Java:

### Opción A: Usando el script personalizado (Recomendado si se tiene varias versiones de Java)
Utilizar el script `build.ps1` que fuerza el uso del JDK 25 local:

```powershell
.\build.ps1 quarkus:dev
```

### Opción B: Usando el Maven Wrapper original
Si la versión global de Java es la 25 o superior:

```shell script
./mvnw quarkus:dev
```

> La interfaz de **Swagger UI** estará disponible en: [http://localhost:8080/q/swagger-ui/](http://localhost:8080/q/swagger-ui/)

## Empaquetado y Ejecución

Para generar el archivo ejecutable de la aplicación:

```shell script
./mvnw package
```

O con el script personalizado:

```powershell
.\build.ps1 package
```

Esto produce el archivo `quarkus-run.jar` en la carpeta `target/quarkus-app/`. Se puede ejecutar con:
`java -jar target/quarkus-app/quarkus-run.jar`.

## Guías de Quarkus Relacionadas

*   JDBC Driver - H2 ([guía](https://quarkus.io/guides/datasource))
*   SmallRye OpenAPI/Swagger ([guía](https://quarkus.io/guides/openapi-swaggerui))
*   Hibernate ORM con Panache ([guía](https://quarkus.io/guides/hibernate-orm-panache))
*   Seguridad con JWT ([guía](https://quarkus.io/guides/security-jwt))
*   JDBC Driver - MySQL ([guía](https://quarkus.io/guides/datasource))
