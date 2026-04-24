package com.colpix.challenge.repository;

import com.colpix.challenge.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

/**
 * Repositorio para la gestión de datos de empleados.
 */
public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    
    /**
     * Busca un empleado por su dirección de email.
     */
    Optional<Employee> findByEmail(String email);

    /**
     * Cuenta la cantidad de empleados a cargo (directos e indirectos) usando una CTE recursiva.
     * @param id ID del supervisor.
     * @return Cantidad total de subordinados.
     */
    @Query(value = """
        WITH RECURSIVE Subordinates AS (
            SELECT id FROM employees WHERE supervisor_id = :id
            UNION ALL
            SELECT e.id FROM employees e
            INNER JOIN Subordinates s ON e.supervisor_id = s.id
        )
        SELECT COUNT(*) FROM Subordinates
        """, nativeQuery = true)
    long countSubordinates(@Param("id") Long id);
}
