package com.colpix.challenge.service;

import com.colpix.challenge.dto.AuthResponse;
import com.colpix.challenge.dto.LoginRequest;
import com.colpix.challenge.repository.UserRepository;
import com.colpix.challenge.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * Servicio encargado de la autenticación y generación de tokens JWT.
 */
@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    /**
     * Valida las credenciales del usuario y retorna un token JWT.
     */
    public AuthResponse login(LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );

        var user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));

        var token = jwtService.generateToken(user);

        return AuthResponse.builder()
                .token(token)
                .expiresIn(jwtService.getExpirationTime())
                .build();
    }
}
