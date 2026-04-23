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
 * Inicializador de datos para pruebas. 
 * Configura un usuario administrador y una estructura inicial de empleados.
 */
@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final EmployeeRepository employeeRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        // Crear usuario admin si no existe
        if (userRepository.findByUsername("admin").isEmpty()) {
            User admin = User.builder()
                    .username("admin")
                    .password(passwordEncoder.encode("admin123"))
                    .build();
            userRepository.save(admin);
        }

        // Crear estructura de empleados de prueba
        if (employeeRepository.count() == 0) {
            Employee ceo = Employee.builder()
                    .name("Elon Musk")
                    .email("elon@colpix.com")
                    .build();
            ceo = employeeRepository.save(ceo);

            Employee manager = Employee.builder()
                    .name("Gwynne Shotwell")
                    .email("gwynne@colpix.com")
                    .supervisorId(ceo.getId())
                    .build();
            manager = employeeRepository.save(manager);

            Employee dev = Employee.builder()
                    .name("John Doe")
                    .email("john@colpix.com")
                    .supervisorId(manager.getId())
                    .build();
            employeeRepository.save(dev);
        }
    }
}
