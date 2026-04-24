package com.colpix.challenge.service;

import com.colpix.challenge.dto.EmployeeDetailResponse;
import com.colpix.challenge.dto.EmployeeRequest;
import com.colpix.challenge.dto.EmployeeResponse;
import com.colpix.challenge.model.Employee;
import com.colpix.challenge.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

/**
 * Servicio para la lógica de negocio relacionada con empleados.
 * Aplica principios SOLID y procesamiento paralelo.
 */
@Service
@RequiredArgsConstructor
public class EmployeeService {

    private final EmployeeRepository employeeRepository;

    /**
     * Crea un nuevo empleado.
     */
    public EmployeeResponse create(EmployeeRequest request) {
        if (employeeRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new RuntimeException("El email ya está registrado");
        }

        Employee employee = Employee.builder()
                .name(request.getName())
                .email(request.getEmail())
                .supervisorId(request.getSupervisorId())
                .build();

        Employee saved = employeeRepository.save(employee);
        return mapToResponse(saved);
    }

    /**
     * Actualiza un empleado existente.
     */
    public EmployeeResponse update(Long id, EmployeeRequest request) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Empleado no encontrado"));

        // Validar si el nuevo email ya existe en otro empleado
        employeeRepository.findByEmail(request.getEmail())
                .ifPresent(existing -> {
                    if (!existing.getId().equals(id)) {
                        throw new RuntimeException("El email ya está siendo usado por otro empleado");
                    }
                });

        employee.setName(request.getName());
        employee.setEmail(request.getEmail());
        employee.setSupervisorId(request.getSupervisorId());

        Employee saved = employeeRepository.save(employee);
        return mapToResponse(saved);
    }

    /**
     * Obtiene todos los empleados registrados.
     */
    public List<EmployeeResponse> getAllEmployees() {
        return employeeRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    /**
     * Obtiene el detalle de un empleado por ID, incluyendo conteo de subordinados en paralelo.
     */
    public EmployeeDetailResponse getEmployeeDetail(Long id) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Empleado no encontrado"));

        // Procesamiento en paralelo para obtener detalles y conteo de personal a cargo
        CompletableFuture<Long> reportsCountFuture = CompletableFuture.supplyAsync(() -> 
            employeeRepository.countSubordinates(id)
        );

        return EmployeeDetailResponse.builder()
                .id(employee.getId())
                .name(employee.getName())
                .email(employee.getEmail())
                .supervisorId(employee.getSupervisorId())
                .updatedAt(employee.getUpdatedAt())
                .reportsCount(reportsCountFuture.join())
                .build();
    }

    /**
     * Mapea una entidad Employee a su DTO de respuesta básico.
     */
    private EmployeeResponse mapToResponse(Employee employee) {
        return EmployeeResponse.builder()
                .id(employee.getId())
                .name(employee.getName())
                .email(employee.getEmail())
                .supervisorId(employee.getSupervisorId())
                .updatedAt(employee.getUpdatedAt())
                .build();
    }
}
