# Colpix Backend - Code Challenge Solution

Este proyecto es una solución robusta y profesional al desafío técnico de Colpix. Se ha diseñado priorizando la mantenibilidad, la seguridad y el rendimiento.

## 🚀 Arquitectura y Tecnologías
- **Spring Boot 3.2.5** & **Java 21**
- **Spring Security + JWT**: Autenticación y autorización parametrizable.
- **MySQL 8**: Persistencia de datos.
- **Docker & Docker Compose**: Orquestación de contenedores.
- **H2 Database**: Base de datos en memoria para ejecución de tests de integración aislados.

### Puntos Destacados del Desarrollo
1. **Procesamiento en Paralelo**: Cumpliendo con el requerimiento de performance, el detalle de empleados y el conteo de subordinados se ejecutan de forma asíncrona usando `CompletableFuture`, optimizando los tiempos de respuesta.
2. **Consultas Recursivas**: El conteo de empleados a cargo (directos e indirectos) se realiza mediante **Common Table Expressions (CTE) recursivas** directamente en la base de datos, garantizando eficiencia incluso con grandes jerarquías.
3. **Manejo Global de Excepciones**: Se implementó un `@RestControllerAdvice` para estandarizar las respuestas de error (400, 401, 404, 409, 500) en un formato JSON consistente.
4. **Validaciones Estrictas**: Lógica de negocio protegida contra duplicados de email, supervisores inexistentes y estados de autenticación inválidos.

---

## 🧪 Estrategia de Testing (Aseguramiento de Calidad)
Se ha implementado una suite de pruebas doble para garantizar la estabilidad del sistema:

### 1. Tests Unitarios (`src/test/java/.../service`)
Prueban la lógica de negocio de forma aislada usando **JUnit 5** y **Mockito**.
- Cobertura de flujos principales y casos de error (excepciones).
- Mockeo de dependencias para velocidad de ejecución.

### 2. Tests de Integración (`src/test/java/.../controller/*IT.java`)
Prueban el sistema de punta a punta, incluyendo la capa de persistencia y seguridad.
- **BaseIT**: Clase base con configuración centralizada que levanta el contexto de Spring.
- **H2 en Modo MySQL**: Permite ejecutar las queries recursivas nativas en un entorno controlado y volátil.
- **Rollback Automático**: Cada test limpia su estado mediante la anotación `@Transactional`.

---

## 🛠️ Cómo Probar la Solución (Paso a Paso)

### 1. Levantar el Entorno con Docker
```bash
docker-compose up -d --build
```
La API estará disponible en `http://localhost:48080` (Puerto configurado en `.env`).

### 2. Variables de Entorno (.env)
Para que el sistema funcione correctamente, se debe contar con un archivo `.env` en la raíz con el siguiente contenido (ya incluido en el proyecto):

```env
# Database Configuration
MYSQL_DATABASE=colpix_db
MYSQL_ROOT_PASSWORD=root_password
MYSQL_USER=colpix_user
MYSQL_PASSWORD=colpix_password
MYSQL_PORT=43306

# Backend Configuration
BACKEND_PORT=48080

# Security Configuration
JWT_SECRET=5367566B59703373367639792F423F4528482B4D6251655468576D5A71347437
JWT_EXPIRATION=300000

# Local Configuration (Spring Boot direct run)
SPRING_DATASOURCE_URL=jdbc:mysql://localhost:43306/colpix_db?allowPublicKeyRetrieval=true&useSSL=false
SPRING_DATASOURCE_USERNAME=colpix_user
SPRING_DATASOURCE_PASSWORD=colpix_password
```

> **Nota para desarrollo local**: Si ejecutas la aplicación directamente (vía IDE o `mvn spring-boot:run`), el puerto por defecto será el `8080`.

### 3. Ejecutar los Tests (Lo que tu entrevistador debe ver)
Para validar la calidad del código y la integridad de las funciones:
```bash
mvn clean test
```
Esto ejecutará tanto los tests unitarios como los de integración, mostrando un reporte de éxito en la consola.

### 3. Flujo de API Manual
Se incluye una **Postman Collection** en la raíz del proyecto (`Colpix_Challenge.postman_collection.json`). El flujo recomendado es:
1. **Login**: Obtener el token con usuario `admin` y clave `admin123`.
2. **Registrar Empleado**: Usar el token obtenido (Bearer Token) para crear nuevos registros.
3. **Consultar Detalle**: Verificar que el campo `reportsCount` devuelva la jerarquía correcta de subordinados (directos e indirectos).

---

## 📐 Diseño de Código (Principios Aplicados)
- **SOLID**: Responsabilidades segregadas entre Controladores, Servicios y Repositorios.
- **DRY (Don't Repeat Yourself)**: Uso de DTOs y una jerarquía de excepciones común.
- **Clean Code**: Nombres de variables descriptivos y código autodescriptivo. Los comentarios están en castellano para facilitar la lectura del equipo local, mientras que los mensajes de error y la lógica siguen convenciones internacionales en inglés.
