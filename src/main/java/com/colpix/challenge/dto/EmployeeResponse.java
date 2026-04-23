package com.colpix.challenge.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Respuesta básica con los detalles de un empleado.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeResponse {
    private Long id;
    private String name;
    private String email;
    private Long supervisorId;
    private LocalDateTime updatedAt;
}
