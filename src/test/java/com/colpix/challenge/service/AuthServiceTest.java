package com.colpix.challenge.service;

import com.colpix.challenge.dto.AuthResponse;
import com.colpix.challenge.dto.LoginRequest;
import com.colpix.challenge.model.User;
import com.colpix.challenge.repository.UserRepository;
import com.colpix.challenge.security.JwtService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private JwtService jwtService;

    @Mock
    private AuthenticationManager authenticationManager;

    @InjectMocks
    private AuthService authService;

    private LoginRequest loginRequest;
    private User user;

    @BeforeEach
    void setUp() {
        loginRequest = new LoginRequest("admin", "password");
        user = User.builder()
                .id(1L)
                .username("admin")
                .password("encodedPassword")
                .build();
    }

    @Test
    void login_ShouldReturnToken_WhenCredentialsAreValid() {
        // Arrange
        when(userRepository.findByUsername(loginRequest.getUsername())).thenReturn(Optional.of(user));
        when(jwtService.generateToken(user)).thenReturn("mocked-jwt-token");
        when(jwtService.getExpirationTime()).thenReturn(3600L);

        // Act
        AuthResponse response = authService.login(loginRequest);

        // Assert
        assertNotNull(response);
        assertEquals("mocked-jwt-token", response.getToken());
        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
    }

    @Test
    void login_ShouldThrowException_WhenAuthenticationFails() {
        // Arrange
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new BadCredentialsException("Invalid credentials"));

        // Act & Assert
        assertThrows(BadCredentialsException.class, () -> authService.login(loginRequest));
        verify(jwtService, never()).generateToken(any());
    }

    @Test
    void login_ShouldThrowUserNotFound_WhenUserDoesNotExistInDb() {
        // Arrange
        when(userRepository.findByUsername(loginRequest.getUsername())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(UsernameNotFoundException.class, () -> authService.login(loginRequest));
    }
}
