package com.colpix.challenge.repository;

import com.colpix.challenge.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Repositorio para la gestión de usuarios de autenticación.
 */
public interface UserRepository extends JpaRepository<User, Long> {
    /**
     * Busca un usuario por su nombre de usuario.
     */
    Optional<User> findByUsername(String username);
}
