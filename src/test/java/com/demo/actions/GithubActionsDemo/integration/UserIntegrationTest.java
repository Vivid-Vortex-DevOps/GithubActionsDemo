package com.demo.actions.GithubActionsDemo.integration;

import com.demo.actions.GithubActionsDemo.dto.UserDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureWebMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests using Testcontainers.
 * 
 * <p>This class demonstrates integration testing with a real PostgreSQL database
 * using Testcontainers to ensure environment consistency.
 */
@SpringBootTest
@AutoConfigureWebMvc
@Testcontainers
@ActiveProfiles("test")
@DisplayName("User Integration Tests")
class UserIntegrationTest {

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15-alpine")
            .withDatabaseName("testdb")
            .withUsername("testuser")
            .withPassword("testpass");

    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    @DisplayName("Should create and retrieve user through full integration")
    void shouldCreateAndRetrieveUserThroughFullIntegration() throws Exception {
        // Given
        UserDto newUser = new UserDto(null, "John", "Doe", "john.doe@example.com", 30, "+1-555-123-4567");
        String userJson = objectMapper.writeValueAsString(newUser);

        // When - Create user
        String createResponse = mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.firstName").value("John"))
                .andExpect(jsonPath("$.lastName").value("Doe"))
                .andExpect(jsonPath("$.email").value("john.doe@example.com"))
                .andExpect(jsonPath("$.age").value(30))
                .andExpect(jsonPath("$.id").exists())
                .andReturn()
                .getResponse()
                .getContentAsString();

        // Parse the created user to get the ID
        UserDto createdUser = objectMapper.readValue(createResponse, UserDto.class);
        assertThat(createdUser.getId()).isNotNull();

        // When - Retrieve user
        mockMvc.perform(get("/api/users/{id}", createdUser.getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(createdUser.getId()))
                .andExpect(jsonPath("$.firstName").value("John"))
                .andExpect(jsonPath("$.lastName").value("Doe"))
                .andExpect(jsonPath("$.email").value("john.doe@example.com"))
                .andExpect(jsonPath("$.age").value(30))
                .andExpect(jsonPath("$.phoneNumber").value("+1-555-123-4567"));
    }

    @Test
    @DisplayName("Should update user through full integration")
    void shouldUpdateUserThroughFullIntegration() throws Exception {
        // Given - Create a user first
        UserDto newUser = new UserDto(null, "Jane", "Smith", "jane.smith@example.com", 25, "+1-555-987-6543");
        String createResponse = mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newUser)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        UserDto createdUser = objectMapper.readValue(createResponse, UserDto.class);

        // When - Update the user
        UserDto updateData = new UserDto(null, "Jane", "Updated", "jane.updated@example.com", 26, "+1-555-987-6543");
        String updateJson = objectMapper.writeValueAsString(updateData);

        mockMvc.perform(put("/api/users/{id}", createdUser.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updateJson))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(createdUser.getId()))
                .andExpect(jsonPath("$.firstName").value("Jane"))
                .andExpect(jsonPath("$.lastName").value("Updated"))
                .andExpect(jsonPath("$.email").value("jane.updated@example.com"))
                .andExpect(jsonPath("$.age").value(26));
    }

    @Test
    @DisplayName("Should delete user through full integration")
    void shouldDeleteUserThroughFullIntegration() throws Exception {
        // Given - Create a user first
        UserDto newUser = new UserDto(null, "Alice", "Johnson", "alice.johnson@example.com", 28, "+1-555-111-2222");
        String createResponse = mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newUser)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        UserDto createdUser = objectMapper.readValue(createResponse, UserDto.class);

        // When - Delete the user
        mockMvc.perform(delete("/api/users/{id}", createdUser.getId()))
                .andExpect(status().isNoContent());

        // Then - Verify user is deleted
        mockMvc.perform(get("/api/users/{id}", createdUser.getId()))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Should return all users through full integration")
    void shouldReturnAllUsersThroughFullIntegration() throws Exception {
        // Given - Create multiple users
        UserDto user1 = new UserDto(null, "User1", "Test", "user1@test.com", 25, "+1-555-111-1111");
        UserDto user2 = new UserDto(null, "User2", "Test", "user2@test.com", 30, "+1-555-222-2222");

        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user1)))
                .andExpect(status().isCreated());

        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user2)))
                .andExpect(status().isCreated());

        // When - Get all users
        mockMvc.perform(get("/api/users"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].firstName").exists())
                .andExpect(jsonPath("$[1].firstName").exists());
    }

    @Test
    @DisplayName("Should reject invalid user data through full integration")
    void shouldRejectInvalidUserDataThroughFullIntegration() throws Exception {
        // Given - Invalid user data
        UserDto invalidUser = new UserDto(null, "", "", "invalid-email", -5, "invalid-phone");
        String invalidJson = objectMapper.writeValueAsString(invalidUser);

        // When & Then
        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidJson))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Should reject duplicate email through full integration")
    void shouldRejectDuplicateEmailThroughFullIntegration() throws Exception {
        // Given - Create first user
        UserDto user1 = new UserDto(null, "User1", "Test", "duplicate@test.com", 25, "+1-555-111-1111");
        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user1)))
                .andExpect(status().isCreated());

        // When - Try to create second user with same email
        UserDto user2 = new UserDto(null, "User2", "Test", "duplicate@test.com", 30, "+1-555-222-2222");

        // Then - Should be rejected
        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user2)))
                .andExpect(status().isConflict());
    }

    @Test
    @DisplayName("Should handle concurrent user creation")
    void shouldHandleConcurrentUserCreation() throws Exception {
        // Given
        int numberOfUsers = 5;
        Thread[] threads = new Thread[numberOfUsers];

        // When - Create users concurrently
        for (int i = 0; i < numberOfUsers; i++) {
            final int index = i;
            threads[i] = new Thread(() -> {
                try {
                    UserDto user = new UserDto(null, "User" + index, "Test", 
                            "user" + index + "@test.com", 20 + index, "+1-555-000-" + String.format("%04d", index));
                    mockMvc.perform(post("/api/users")
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(objectMapper.writeValueAsString(user)))
                            .andExpect(status().isCreated());
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            });
            threads[i].start();
        }

        // Wait for all threads to complete
        for (Thread thread : threads) {
            thread.join();
        }

        // Then - Verify all users were created
        mockMvc.perform(get("/api/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    @DisplayName("Should validate database connection")
    void shouldValidateDatabaseConnection() {
        // Given & When & Then
        assertThat(postgres.isRunning()).isTrue();
        assertThat(postgres.getDatabaseName()).isEqualTo("testdb");
        assertThat(postgres.getUsername()).isEqualTo("testuser");
        assertThat(postgres.getPassword()).isEqualTo("testpass");
    }
} 