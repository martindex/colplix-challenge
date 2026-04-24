# Colpix Backend - Solución al Desafío Técnico

Este repositorio contiene una solución de grado empresarial para el desafío técnico de **Colpix**. La arquitectura ha sido diseñada siguiendo principios **SOLID**, patrones de diseño modernos y un enfoque en la eficiencia operativa.

## 🚀 Arquitectura y Stack Tecnológico
- **Core**: Spring Boot 3.2.5 & Java 21 (LTS).
- **Seguridad**: Spring Security 6 con autenticación **Stateless (JWT)**.
- **Persistencia**: MySQL 8.0 para producción y H2 para entornos de testing.
- **Infraestructura**: Docker & Docker Compose con gestión de secretos vía variables de entorno (`.env`).
- **Documentación**: OpenAPI 3 (Swagger) integrada para exploración interactiva.

## 💡 Decisones Técnicas y Puntos Destacados

### 1. Procesamiento Paralelo y Concurrencia (Requerimiento #4)
Para maximizar el rendimiento en la obtención de detalles jerárquicos, se implementó un modelo de ejecución asíncrona:
- Se utiliza `CompletableFuture` para disparar en paralelo la recuperación de la entidad base y el cálculo de la jerarquía.
- Esto reduce el tiempo total de respuesta al tiempo de la tarea más lenta, en lugar de la suma de ambas.

### 2. Eficiencia en Consultas Jerárquicas
El conteo de subordinados (directos e indirectos) se resuelve mediante una **Recursive CTE (Common Table Expression)** nativa de MySQL. 
- **Ventaja**: Se evita el problema de *N+1 queries* y la recursividad en memoria de Java, delegando la carga al motor de base de datos de forma optimizada.

### 3. Seguridad y Extensibilidad
- **Filtro JWT Personalizado**: Intercepta y valida tokens en cada petición, manteniendo el estado de la sesión fuera del servidor.
- **Expiración Parametrizable**: Configurable desde el entorno, permitiendo ajustes rápidos sin cambios de código.

### 4. Calidad de Código y Estándares
- **Nomenclatura**: Código fuente íntegramente en inglés siguiendo estándares internacionales.
- **Documentación**: Comentarios en castellano para facilitar la revisión del equipo técnico local, con Javadoc detallado en componentes clave.
- **SOLID**: Segregación clara de responsabilidades; los controladores solo gestionan el flujo HTTP, mientras que la lógica reside en la capa de servicio.

## 🧪 Estrategia de Pruebas
Se ha priorizado la estabilidad mediante una suite de tests robusta:
- **Unit Tests**: Validación de lógica de negocio aislada con JUnit 5 y Mockito.
- **Integration Tests (IT)**: Pruebas de punta a punta con base de datos real (H2 en modo MySQL) para validar la seguridad y la persistencia.

---

## 🛠️ Guía de Ejecución

### Requisitos Previos
- Docker y Docker Compose instalados.
- Puerto `48080` (Backend) y `43306` (MySQL) disponibles (o configurables en `.env`).

### Despliegue Rápido
```bash
docker-compose up -d --build
```

### Acceso a la Documentación (Swagger)
Una vez levantado el entorno, el entrevistador puede explorar y probar la API en:
👉 **[http://localhost:48080/swagger-ui.html](http://localhost:48080/swagger-ui.html)**

### Ejecución de Tests
```bash
mvn clean test
```

## 📬 Recursos Adicionales
- **Postman Collection**: [Colpix_Challenge.postman_collection.json](file:///c:/Users/martin.tapia/workspaces/colplix/colplix-challenge/Colpix_Challenge.postman_collection.json) incluida para pruebas manuales simplificadas.
- **Archivo .env**: Contiene la configuración base necesaria para el arranque inmediato.

---
*Desarrollado con foco en la calidad y la escalabilidad para el equipo de Colpix.*
