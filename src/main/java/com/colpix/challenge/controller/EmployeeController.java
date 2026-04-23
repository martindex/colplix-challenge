package com.colpix.challenge.controller;

import com.colpix.challenge.dto.EmployeeDetailResponse;
import com.colpix.challenge.dto.EmployeeRequest;
import com.colpix.challenge.dto.EmployeeResponse;
import com.colpix.challenge.service.EmployeeService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST para la gestión de empleados.
 */
@RestController
@RequestMapping("/api/employees")
@RequiredArgsConstructor
@Tag(name = "Empleados", description = "Endpoints para la gestión y consulta de empleados")
public class EmployeeController {

    private final EmployeeService employeeService;

    /**
     * Endpoint para registrar o actualizar un empleado.
     */
    @PostMapping
    public ResponseEntity<EmployeeResponse> saveOrUpdate(@Valid @RequestBody EmployeeRequest request) {
        return ResponseEntity.ok(employeeService.saveOrUpdate(request));
    }

    /**
     * Endpoint para obtener el listado de todos los empleados.
     */
    @GetMapping
    public ResponseEntity<List<EmployeeResponse>> getAllEmployees() {
        return ResponseEntity.ok(employeeService.getAllEmployees());
    }

    /**
     * Endpoint para obtener el detalle de un empleado específico por su ID.
     */
    @GetMapping("/{id}")
    public ResponseEntity<EmployeeDetailResponse> getEmployeeDetail(@PathVariable Long id) {
        return ResponseEntity.ok(employeeService.getEmployeeDetail(id));
    }
}
