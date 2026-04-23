package com.colpix.challenge.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Objeto de transferencia de datos para crear o actualizar un empleado.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeRequest {
    @NotBlank(message = "Nombre es requerido")
    private String name;

    @NotBlank(message = "Email es requerido")
    @Email(message = "Email debe ser válido")
    private String email;

    private Long supervisorId;
}
