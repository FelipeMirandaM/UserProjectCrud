# UserProjectCrud

Proyecto Spring boot que permite administrar una tabla de usuarios con telefonos, contiene data precargada en H2 y cuenta con documentación Swagger que se actualiza de forma semi automatica.

## Requisitos
1. JDK 11 o superior instalado.
2. Git.
3. (Opcional) IntelliJ IDEA 2025.1 para abrir y ejecutar el proyecto.

## Como ejecutar el proyecto
1. Clonar el repositorio
2. Abrir desde IDE, en este caso IntelliJ IDEA 2025.1
3. Ejecutar UserCrudApplication.java como una aplicación Java.
4. El servidor se iniciará en `http://localhost:8080`.
5. Para consumir un servicio primero se debe generar un token utilizando el servicio `/api/v1/token` y luego entregar el token generado como Bearer token a los otros servicios de la API. 
6. Para acceder a la documentación Swagger, ir a `http://localhost:8080/swagger-ui/index.html`.
