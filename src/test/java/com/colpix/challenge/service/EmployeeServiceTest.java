package com.colpix.challenge.service;

import com.colpix.challenge.dto.EmployeeDetailResponse;
import com.colpix.challenge.dto.EmployeeRequest;
import com.colpix.challenge.dto.EmployeeResponse;
import com.colpix.challenge.exception.ConflictException;
import com.colpix.challenge.exception.ResourceNotFoundException;
import com.colpix.challenge.model.Employee;
import com.colpix.challenge.repository.EmployeeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmployeeServiceTest {

    @Mock
    private EmployeeRepository employeeRepository;

    @InjectMocks
    private EmployeeService employeeService;

    private EmployeeRequest validRequest;
    private Employee employee;

    @BeforeEach
    void setUp() {
        validRequest = EmployeeRequest.builder()
                .name("John Doe")
                .email("john.doe@colpix.com")
                .supervisorId(1L)
                .build();

        employee = Employee.builder()
                .id(100L)
                .name("John Doe")
                .email("john.doe@colpix.com")
                .supervisorId(1L)
                .build();
    }

    @Test
    void create_ShouldCreateEmployee_WhenRequestIsValid() {
        // Arrange
        when(employeeRepository.findByEmail(validRequest.getEmail())).thenReturn(Optional.empty());
        when(employeeRepository.existsById(1L)).thenReturn(true);
        when(employeeRepository.save(any(Employee.class))).thenReturn(employee);

        // Act
        EmployeeResponse response = employeeService.create(validRequest);

        // Assert
        assertNotNull(response);
        assertEquals(employee.getEmail(), response.getEmail());
        verify(employeeRepository).save(any(Employee.class));
    }

    @Test
    void create_ShouldThrowConflict_WhenEmailAlreadyExists() {
        // Arrange
        when(employeeRepository.findByEmail(validRequest.getEmail())).thenReturn(Optional.of(employee));

        // Act & Assert
        assertThrows(ConflictException.class, () -> employeeService.create(validRequest));
        verify(employeeRepository, never()).save(any(Employee.class));
    }

    @Test
    void create_ShouldThrowNotFound_WhenSupervisorDoesNotExist() {
        // Arrange
        when(employeeRepository.findByEmail(validRequest.getEmail())).thenReturn(Optional.empty());
        when(employeeRepository.existsById(1L)).thenReturn(false);

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> employeeService.create(validRequest));
    }

    @Test
    void update_ShouldUpdateEmployee_WhenIdAndRequestAreValid() {
        // Arrange
        Long id = 100L;
        when(employeeRepository.findById(id)).thenReturn(Optional.of(employee));
        when(employeeRepository.findByEmail(validRequest.getEmail())).thenReturn(Optional.of(employee)); // same employee
        when(employeeRepository.existsById(1L)).thenReturn(true);
        when(employeeRepository.save(any(Employee.class))).thenReturn(employee);

        // Act
        EmployeeResponse response = employeeService.update(id, validRequest);

        // Assert
        assertNotNull(response);
        verify(employeeRepository).save(any(Employee.class));
    }

    @Test
    void update_ShouldThrowConflict_WhenEmailInUseByAnotherEmployee() {
        // Arrange
        Long id = 100L;
        Employee anotherEmployee = Employee.builder().id(200L).email("john.doe@colpix.com").build();
        
        when(employeeRepository.findById(id)).thenReturn(Optional.of(employee));
        when(employeeRepository.findByEmail(validRequest.getEmail())).thenReturn(Optional.of(anotherEmployee));

        // Act & Assert
        assertThrows(ConflictException.class, () -> employeeService.update(id, validRequest));
    }

    @Test
    void getEmployeeDetail_ShouldReturnDetailWithReportCount() {
        // Arrange
        Long id = 100L;
        when(employeeRepository.findById(id)).thenReturn(Optional.of(employee));
        when(employeeRepository.countSubordinates(id)).thenReturn(5L);

        // Act
        EmployeeDetailResponse response = employeeService.getEmployeeDetail(id);

        // Assert
        assertNotNull(response);
        assertEquals(5L, response.getReportsCount());
        verify(employeeRepository).countSubordinates(id);
    }
}
