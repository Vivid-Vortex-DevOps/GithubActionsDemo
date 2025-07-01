package com.demo.actions.GithubActionsDemo.controller;

import com.demo.actions.GithubActionsDemo.dto.UserDto;
import com.demo.actions.GithubActionsDemo.exception.UserNotFoundException;
import com.demo.actions.GithubActionsDemo.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for UserController.
 * 
 * <p>This class demonstrates comprehensive testing using AssertJ, MockMvc,
 * and proper test structure with descriptive test names.
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("User Controller Tests")
class UserControllerTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    private UserDto testUser;
    private List<UserDto> testUsers;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
        objectMapper = new ObjectMapper();

        // Setup test data
        testUser = new UserDto(1L, "John", "Doe", "john.doe@example.com", 30, "+1-555-123-4567");
        testUsers = Arrays.asList(
                testUser,
                new UserDto(2L, "Jane", "Smith", "jane.smith@example.com", 25, "+1-555-987-6543")
        );
    }

    @Test
    @DisplayName("Should return all users successfully")
    void shouldReturnAllUsers() throws Exception {
        // Given
        when(userService.getAllUsers()).thenReturn(testUsers);

        // When & Then
        mockMvc.perform(get("/api/users"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].firstName").value("John"))
                .andExpect(jsonPath("$[0].lastName").value("Doe"))
                .andExpect(jsonPath("$[0].email").value("john.doe@example.com"))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].firstName").value("Jane"));

        verify(userService, times(1)).getAllUsers();
    }

    @Test
    @DisplayName("Should return user by ID successfully")
    void shouldReturnUserById() throws Exception {
        // Given
        Long userId = 1L;
        when(userService.getUserById(userId)).thenReturn(testUser);

        // When & Then
        mockMvc.perform(get("/api/users/{id}", userId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.firstName").value("John"))
                .andExpect(jsonPath("$.lastName").value("Doe"))
                .andExpect(jsonPath("$.email").value("john.doe@example.com"))
                .andExpect(jsonPath("$.age").value(30))
                .andExpect(jsonPath("$.phoneNumber").value("+1-555-123-4567"));

        verify(userService, times(1)).getUserById(userId);
    }

    @Test
    @DisplayName("Should return 404 when user not found")
    void shouldReturn404WhenUserNotFound() throws Exception {
        // Given
        Long userId = 999L;
        when(userService.getUserById(userId)).thenThrow(new UserNotFoundException("User not found"));

        // When & Then
        mockMvc.perform(get("/api/users/{id}", userId))
                .andExpect(status().isNotFound());

        verify(userService, times(1)).getUserById(userId);
    }

    @Test
    @DisplayName("Should create user successfully")
    void shouldCreateUser() throws Exception {
        // Given
        UserDto newUser = new UserDto(null, "Alice", "Johnson", "alice.johnson@example.com", 28, "+1-555-111-2222");
        UserDto createdUser = new UserDto(3L, "Alice", "Johnson", "alice.johnson@example.com", 28, "+1-555-111-2222");
        
        when(userService.createUser(any(UserDto.class))).thenReturn(createdUser);

        // When & Then
        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newUser)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(3))
                .andExpect(jsonPath("$.firstName").value("Alice"))
                .andExpect(jsonPath("$.lastName").value("Johnson"))
                .andExpect(jsonPath("$.email").value("alice.johnson@example.com"));

        verify(userService, times(1)).createUser(any(UserDto.class));
    }

    @Test
    @DisplayName("Should return 400 when creating user with invalid data")
    void shouldReturn400WhenCreatingUserWithInvalidData() throws Exception {
        // Given
        UserDto invalidUser = new UserDto(null, "", "", "invalid-email", -5, "invalid-phone");

        // When & Then
        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidUser)))
                .andExpect(status().isBadRequest());

        verify(userService, never()).createUser(any(UserDto.class));
    }

    @Test
    @DisplayName("Should update user successfully")
    void shouldUpdateUser() throws Exception {
        // Given
        Long userId = 1L;
        UserDto updateUser = new UserDto(null, "John", "Updated", "john.updated@example.com", 31, "+1-555-123-4567");
        UserDto updatedUser = new UserDto(1L, "John", "Updated", "john.updated@example.com", 31, "+1-555-123-4567");
        
        when(userService.updateUser(eq(userId), any(UserDto.class))).thenReturn(updatedUser);

        // When & Then
        mockMvc.perform(put("/api/users/{id}", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateUser)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.lastName").value("Updated"))
                .andExpect(jsonPath("$.email").value("john.updated@example.com"))
                .andExpect(jsonPath("$.age").value(31));

        verify(userService, times(1)).updateUser(eq(userId), any(UserDto.class));
    }

    @Test
    @DisplayName("Should return 404 when updating non-existent user")
    void shouldReturn404WhenUpdatingNonExistentUser() throws Exception {
        // Given
        Long userId = 999L;
        UserDto updateUser = new UserDto(null, "John", "Updated", "john.updated@example.com", 31, "+1-555-123-4567");
        
        when(userService.updateUser(eq(userId), any(UserDto.class)))
                .thenThrow(new UserNotFoundException("User not found"));

        // When & Then
        mockMvc.perform(put("/api/users/{id}", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateUser)))
                .andExpect(status().isNotFound());

        verify(userService, times(1)).updateUser(eq(userId), any(UserDto.class));
    }

    @Test
    @DisplayName("Should delete user successfully")
    void shouldDeleteUser() throws Exception {
        // Given
        Long userId = 1L;
        doNothing().when(userService).deleteUser(userId);

        // When & Then
        mockMvc.perform(delete("/api/users/{id}", userId))
                .andExpect(status().isNoContent());

        verify(userService, times(1)).deleteUser(userId);
    }

    @Test
    @DisplayName("Should return 404 when deleting non-existent user")
    void shouldReturn404WhenDeletingNonExistentUser() throws Exception {
        // Given
        Long userId = 999L;
        doThrow(new UserNotFoundException("User not found")).when(userService).deleteUser(userId);

        // When & Then
        mockMvc.perform(delete("/api/users/{id}", userId))
                .andExpect(status().isNotFound());

        verify(userService, times(1)).deleteUser(userId);
    }

    @Test
    @DisplayName("Should validate user DTO fields")
    void shouldValidateUserDtoFields() {
        // Given
        UserDto user = new UserDto();

        // When & Then
        assertThat(user.getId()).isNull();
        assertThat(user.getFirstName()).isNull();
        assertThat(user.getLastName()).isNull();
        assertThat(user.getEmail()).isNull();
        assertThat(user.getAge()).isNull();
        assertThat(user.getPhoneNumber()).isNull();
    }

    @Test
    @DisplayName("Should set and get user DTO fields correctly")
    void shouldSetAndGetUserDtoFields() {
        // Given
        UserDto user = new UserDto();
        Long id = 1L;
        String firstName = "John";
        String lastName = "Doe";
        String email = "john.doe@example.com";
        Integer age = 30;
        String phoneNumber = "+1-555-123-4567";

        // When
        user.setId(id);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setEmail(email);
        user.setAge(age);
        user.setPhoneNumber(phoneNumber);

        // Then
        assertThat(user.getId()).isEqualTo(id);
        assertThat(user.getFirstName()).isEqualTo(firstName);
        assertThat(user.getLastName()).isEqualTo(lastName);
        assertThat(user.getEmail()).isEqualTo(email);
        assertThat(user.getAge()).isEqualTo(age);
        assertThat(user.getPhoneNumber()).isEqualTo(phoneNumber);
    }

    @Test
    @DisplayName("Should test user DTO equals and hashCode")
    void shouldTestUserDtoEqualsAndHashCode() {
        // Given
        UserDto user1 = new UserDto(1L, "John", "Doe", "john@example.com", 30, "+1-555-123-4567");
        UserDto user2 = new UserDto(1L, "John", "Doe", "john@example.com", 30, "+1-555-123-4567");
        UserDto user3 = new UserDto(2L, "Jane", "Smith", "jane@example.com", 25, "+1-555-987-6543");

        // When & Then
        assertThat(user1).isEqualTo(user2);
        assertThat(user1).isNotEqualTo(user3);
        assertThat(user1.hashCode()).isEqualTo(user2.hashCode());
        assertThat(user1.hashCode()).isNotEqualTo(user3.hashCode());
    }

    @Test
    @DisplayName("Should test user DTO toString")
    void shouldTestUserDtoToString() {
        // Given
        UserDto user = new UserDto(1L, "John", "Doe", "john@example.com", 30, "+1-555-123-4567");

        // When
        String result = user.toString();

        // Then
        assertThat(result).contains("UserDto{");
        assertThat(result).contains("id=1");
        assertThat(result).contains("firstName='John'");
        assertThat(result).contains("lastName='Doe'");
        assertThat(result).contains("email='john@example.com'");
        assertThat(result).contains("age=30");
        assertThat(result).contains("phoneNumber='+1-555-123-4567'");
    }
} 