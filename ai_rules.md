# AI Rules - Colpix Code Challenge

## Design Principles
- **SOLID**: Apply Single Responsibility, Open/Closed, Liskov Substitution, Interface Segregation, and Dependency Inversion.
- **Clean Code**: Meaningful names, small methods, and clear intent.
- **DRY**: Don't Repeat Yourself.

## Coding Standards
- **Naming Convention**: 
    - Variables, Methods, and Classes must be in **English**.
    - Use **camelCase** for variables and methods.
    - Use **PascalCase** for classes and interfaces.
- **Comments**: 
    - Comments must be in **Spanish**.
    - Explain "why" rather than "what" when possible.
- **API Standards**:
    - Follow RESTful principles.
    - Use appropriate HTTP status codes.
    - Use DTOs for data transfer.

## Infrastructure
- Use `.env` files for all sensitive or environment-specific configurations.
- Docker Compose services should be named logically (e.g., `backend`, `db`).
