package com.colpix.challenge.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Respuesta detallada de un empleado, incluyendo la cuenta de personal a cargo.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeDetailResponse {
    private Long id;
    private String name;
    private String email;
    private Long supervisorId;
    private LocalDateTime updatedAt;
    private Long reportsCount; // Cantidad de empleados a cargo (directos o indirectos)
}
