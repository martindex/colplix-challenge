package com.colpix.challenge.controller;

import com.colpix.challenge.BaseIT;
import com.colpix.challenge.dto.EmployeeRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Transactional
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@DisplayName("EmployeeController Integration Tests")
class EmployeeControllerIT extends BaseIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private static final String API_URL = "/api/employees";

    @Test
    @WithMockUser(username = "admin")
    @DisplayName("Should list all employees seeded by DataInitializer")
    void getAllEmployees_shouldReturnSeededList() throws Exception {
        mockMvc.perform(get(API_URL))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[0].name", is("Elon Musk")));
    }

    @Test
    @WithMockUser(username = "admin")
    @DisplayName("Should create a new employee successfully")
    void create_shouldSaveEmployee() throws Exception {
        EmployeeRequest request = EmployeeRequest.builder()
                .name("New Dev")
                .email("newdev@colpix.com")
                .supervisorId(1L) // CEO
                .build();

        mockMvc.perform(post(API_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("New Dev")))
                .andExpect(jsonPath("$.email", is("newdev@colpix.com")));
    }

    @Test
    @WithMockUser(username = "admin")
    @DisplayName("Should return 409 Conflict when email already exists")
    void create_shouldReturnConflict_whenEmailDuplicate() throws Exception {
        EmployeeRequest request = EmployeeRequest.builder()
                .name("Duplicate")
                .email("elon@colpix.com") // Already exists from initializer
                .build();

        mockMvc.perform(post(API_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message", is("Email already registered: elon@colpix.com")));
    }

    @Test
    @WithMockUser(username = "admin")
    @DisplayName("Should return 404 Not Found when supervisor does not exist")
    void create_shouldReturnNotFound_whenSupervisorInvalid() throws Exception {
        EmployeeRequest request = EmployeeRequest.builder()
                .name("Orphan")
                .email("orphan@colpix.com")
                .supervisorId(999L)
                .build();

        mockMvc.perform(post(API_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message", is("Supervisor not found with ID: 999")));
    }

    @Test
    @WithMockUser(username = "admin")
    @DisplayName("Should return employee detail with reports count")
    void getEmployeeDetail_shouldReturnDetailWithCount() throws Exception {
        // ID 1 is Elon Musk (CEO), has 2 subordinates (Gwynne and John Doe via Gwynne)
        mockMvc.perform(get(API_URL + "/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("Elon Musk")))
                .andExpect(jsonPath("$.reportsCount", is(2)));
    }
}
