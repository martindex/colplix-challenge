# Colpix Code Challenge

Se requiere crear una RESTFul API, con las siguientes funcionalidades:

1. **Autenticación**: Permitir hacer login con nombre de usuario y clave. Debe devolver un token de expiración automática (por default de 5 minutos pero debe ser parametrizable).
2. **Gestión de Empleados**: Permitir registrar/actualizar un empleado. El API debe esperar un JSON válido con atributos mínimos: `nombre`, `email`, `supervisor_id`.
3. **Listado General**: Obtener los detalles de todos los empleados y sus datetime de última actualización.
4. **Detalle Individual**: Obtener los detalles de un empleado dado su ID. Además de los atributos indicados, el detalle por ID debe indicar:
    - Datetime de última actualización.
    - Cantidad de empleados a cargo (directos o indirectos).
    - **Importante**: La obtención de detalles del empleado y su cantidad de personal a cargo debe hacerse en **forma paralela** con el objetivo de responder más rápidamente el request.
5. **Seguridad**: Todos los endpoints excepto el de login deben validar el token de sesión y devolver un error adecuado si no hay token, o el mismo es inválido/caducado.

## Requisitos Técnicos
- El código deberá estar subido a GitHub/GitLab.
- Se debe proveer una forma de probar todos los endpoints (Ej: Postman Collection).
- Se evaluará:
    - Definición de la API (estándares, etc).
    - Elección de tecnologías.
    - Calidad del código fuente (buenas prácticas, mantenibilidad).

## Stack Solicitado
- **Spring Boot 3**
- **MySQL**
- **Docker Compose** (con volumen para la base de datos)
