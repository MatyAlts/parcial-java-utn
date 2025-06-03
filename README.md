# Proyecto Parcial UTN-FRM Aplicación Java con JDBC

## Requisitos
- Java 8+
- Gradle

## Ejecución

1. Clonar el repositorio.
2. Abrir una terminal en la raíz del proyecto.
3. Ejecuta:
   ```
   gradlew.bat build
   gradlew.bat run
   ```

La base de datos H2 se crea automáticamente en el archivo `appdb.mv.db`.

## Estructura
- `model/`: Clases de dominio
- `dao/`: Interfaces y DAOs
- `util/`: Utilidades y conexión
- `main/`: Clase principal

## Logging
El sistema usa SLF4J con Logback para logging en consola.

## Notas
- El sistema crea las tablas automáticamente si no existen.
- Incluye validación básica de entradas.
- Incluye una clase genérica de respuesta.
