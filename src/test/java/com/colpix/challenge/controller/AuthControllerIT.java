package com.colpix.challenge.controller;

import com.colpix.challenge.BaseIT;
import com.colpix.challenge.dto.LoginRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("AuthController Integration Tests")
class AuthControllerIT extends BaseIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private static final String LOGIN_URL = "/api/auth/login";

    @Test
    @DisplayName("Should return 200 and token with valid credentials")
    void login_shouldReturnToken_whenCredentialsValid() throws Exception {
        LoginRequest request = new LoginRequest("admin", "admin123");

        mockMvc.perform(post(LOGIN_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token", notNullValue()))
                .andExpect(jsonPath("$.expiresIn", notNullValue()));
    }

    @Test
    @DisplayName("Should return 401 Unauthorized with invalid credentials")
    void login_shouldReturn401_whenCredentialsInvalid() throws Exception {
        LoginRequest request = new LoginRequest("admin", "wrong-password");

        mockMvc.perform(post(LOGIN_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message", org.hamcrest.Matchers.is("Invalid username or password")));
    }

    @Test
    @DisplayName("Should return 401 Unauthorized with non-existent user")
    void login_shouldReturn401_whenUserNotFound() throws Exception {
        LoginRequest request = new LoginRequest("ghost", "password");

        mockMvc.perform(post(LOGIN_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message", org.hamcrest.Matchers.is("Invalid username or password")));
    }
}
