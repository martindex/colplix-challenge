package com.colpix.challenge.config;

import com.colpix.challenge.model.Employee;
import com.colpix.challenge.model.User;
import com.colpix.challenge.repository.EmployeeRepository;
import com.colpix.challenge.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * Inicializador de datos para el desafío técnico.
 * Configura el usuario admin y la jerarquía de empleados solicitada.
 */
@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final EmployeeRepository employeeRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        // Inicializar usuario administrador
        if (userRepository.findByUsername("admin").isEmpty()) {
            User admin = User.builder()
                    .username("admin")
                    .password(passwordEncoder.encode("admin123"))
                    .build();
            userRepository.save(admin);
        }

        // Inicializar jerarquía de empleados si la base de datos está vacía
        if (employeeRepository.count() == 0) {
            // Nivel 1: Raíces
            Employee emp1 = saveEmployee("Empleado 1", "emp1@colpix.com", null);
            saveEmployee("Empleado 4", "emp4@colpix.com", null);

            // Nivel 2: Bajo Empleado 1
            Employee emp2 = saveEmployee("Empleado 2", "emp2@colpix.com", emp1.getId());
            saveEmployee("Empleado 3", "emp3@colpix.com", emp1.getId());
            saveEmployee("Empleado 8", "emp8@colpix.com", emp1.getId());

            // Nivel 3: Bajo Empleado 2
            saveEmployee("Empleado 5", "emp5@colpix.com", emp2.getId());
            Employee emp6 = saveEmployee("Empleado 6", "emp6@colpix.com", emp2.getId());

            // Nivel 4: Bajo Empleado 6
            saveEmployee("Empleado 7", "emp7@colpix.com", emp6.getId());
        }
    }

    /**
     * Método auxiliar para persistir empleados de forma limpia.
     */
    private Employee saveEmployee(String name, String email, Long supervisorId) {
        Employee employee = Employee.builder()
                .name(name)
                .email(email)
                .supervisorId(supervisorId)
                .build();
        return employeeRepository.save(employee);
    }
}
