package com.demo.actions.GithubActionsDemo.service;

import com.demo.actions.GithubActionsDemo.dto.UserDto;
import com.demo.actions.GithubActionsDemo.exception.UserNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * Test class for UserService.
 * 
 * <p>This class demonstrates comprehensive testing using AssertJ and proper
 * test structure with descriptive test names.
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("User Service Tests")
class UserServiceTest {

    private UserService userService;

    private UserDto testUser1;
    private UserDto testUser2;

    @BeforeEach
    void setUp() {
        userService = new UserService();
        
        testUser1 = new UserDto(null, "John", "Doe", "john.doe@example.com", 30, "+1-555-123-4567");
        testUser2 = new UserDto(null, "Jane", "Smith", "jane.smith@example.com", 25, "+1-555-987-6543");
    }

    @Test
    @DisplayName("Should return empty list when no users exist")
    void shouldReturnEmptyListWhenNoUsersExist() {
        // When
        List<UserDto> result = userService.getAllUsers();

        // Then
        assertThat(result).isEmpty();
        assertThat(userService.getUserCount()).isZero();
    }

    @Test
    @DisplayName("Should create user successfully")
    void shouldCreateUserSuccessfully() {
        // When
        UserDto createdUser = userService.createUser(testUser1);

        // Then
        assertThat(createdUser.getId()).isEqualTo(1L);
        assertThat(createdUser.getFirstName()).isEqualTo("John");
        assertThat(createdUser.getLastName()).isEqualTo("Doe");
        assertThat(createdUser.getEmail()).isEqualTo("john.doe@example.com");
        assertThat(createdUser.getAge()).isEqualTo(30);
        assertThat(createdUser.getPhoneNumber()).isEqualTo("+1-555-123-4567");
        
        assertThat(userService.getUserCount()).isEqualTo(1);
        assertThat(userService.userExists(1L)).isTrue();
    }

    @Test
    @DisplayName("Should create multiple users with sequential IDs")
    void shouldCreateMultipleUsersWithSequentialIds() {
        // When
        UserDto createdUser1 = userService.createUser(testUser1);
        UserDto createdUser2 = userService.createUser(testUser2);

        // Then
        assertThat(createdUser1.getId()).isEqualTo(1L);
        assertThat(createdUser2.getId()).isEqualTo(2L);
        assertThat(userService.getUserCount()).isEqualTo(2);
    }

    @Test
    @DisplayName("Should throw exception when creating user with duplicate email")
    void shouldThrowExceptionWhenCreatingUserWithDuplicateEmail() {
        // Given
        userService.createUser(testUser1);
        UserDto duplicateEmailUser = new UserDto(null, "Jane", "Doe", "john.doe@example.com", 25, "+1-555-987-6543");

        // When & Then
        assertThatThrownBy(() -> userService.createUser(duplicateEmailUser))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("User with email john.doe@example.com already exists");
    }

    @Test
    @DisplayName("Should get user by ID successfully")
    void shouldGetUserByIdSuccessfully() {
        // Given
        UserDto createdUser = userService.createUser(testUser1);

        // When
        UserDto retrievedUser = userService.getUserById(createdUser.getId());

        // Then
        assertThat(retrievedUser).isEqualTo(createdUser);
    }

    @Test
    @DisplayName("Should throw UserNotFoundException when getting non-existent user")
    void shouldThrowUserNotFoundExceptionWhenGettingNonExistentUser() {
        // When & Then
        assertThatThrownBy(() -> userService.getUserById(999L))
                .isInstanceOf(UserNotFoundException.class)
                .hasMessage("User not found with ID: 999");
    }

    @Test
    @DisplayName("Should update user successfully")
    void shouldUpdateUserSuccessfully() {
        // Given
        UserDto createdUser = userService.createUser(testUser1);
        UserDto updateData = new UserDto(null, "John", "Updated", "john.updated@example.com", 31, "+1-555-123-4567");

        // When
        UserDto updatedUser = userService.updateUser(createdUser.getId(), updateData);

        // Then
        assertThat(updatedUser.getId()).isEqualTo(createdUser.getId());
        assertThat(updatedUser.getFirstName()).isEqualTo("John");
        assertThat(updatedUser.getLastName()).isEqualTo("Updated");
        assertThat(updatedUser.getEmail()).isEqualTo("john.updated@example.com");
        assertThat(updatedUser.getAge()).isEqualTo(31);
    }

    @Test
    @DisplayName("Should throw UserNotFoundException when updating non-existent user")
    void shouldThrowUserNotFoundExceptionWhenUpdatingNonExistentUser() {
        // Given
        UserDto updateData = new UserDto(null, "John", "Updated", "john.updated@example.com", 31, "+1-555-123-4567");

        // When & Then
        assertThatThrownBy(() -> userService.updateUser(999L, updateData))
                .isInstanceOf(UserNotFoundException.class)
                .hasMessage("User not found with ID: 999");
    }

    @Test
    @DisplayName("Should throw exception when updating user with duplicate email")
    void shouldThrowExceptionWhenUpdatingUserWithDuplicateEmail() {
        // Given
        UserDto user1 = userService.createUser(testUser1);
        UserDto user2 = userService.createUser(testUser2);
        UserDto updateData = new UserDto(null, "John", "Updated", "jane.smith@example.com", 31, "+1-555-123-4567");

        // When & Then
        assertThatThrownBy(() -> userService.updateUser(user1.getId(), updateData))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("User with email jane.smith@example.com already exists");
    }

    @Test
    @DisplayName("Should allow updating user with same email")
    void shouldAllowUpdatingUserWithSameEmail() {
        // Given
        UserDto createdUser = userService.createUser(testUser1);
        UserDto updateData = new UserDto(null, "John", "Updated", "john.doe@example.com", 31, "+1-555-123-4567");

        // When
        UserDto updatedUser = userService.updateUser(createdUser.getId(), updateData);

        // Then
        assertThat(updatedUser.getEmail()).isEqualTo("john.doe@example.com");
        assertThat(updatedUser.getLastName()).isEqualTo("Updated");
        assertThat(updatedUser.getAge()).isEqualTo(31);
    }

    @Test
    @DisplayName("Should delete user successfully")
    void shouldDeleteUserSuccessfully() {
        // Given
        UserDto createdUser = userService.createUser(testUser1);
        assertThat(userService.userExists(createdUser.getId())).isTrue();

        // When
        userService.deleteUser(createdUser.getId());

        // Then
        assertThat(userService.userExists(createdUser.getId())).isFalse();
        assertThat(userService.getUserCount()).isZero();
    }

    @Test
    @DisplayName("Should throw UserNotFoundException when deleting non-existent user")
    void shouldThrowUserNotFoundExceptionWhenDeletingNonExistentUser() {
        // When & Then
        assertThatThrownBy(() -> userService.deleteUser(999L))
                .isInstanceOf(UserNotFoundException.class)
                .hasMessage("User not found with ID: 999");
    }

    @Test
    @DisplayName("Should return all users correctly")
    void shouldReturnAllUsersCorrectly() {
        // Given
        UserDto createdUser1 = userService.createUser(testUser1);
        UserDto createdUser2 = userService.createUser(testUser2);

        // When
        List<UserDto> allUsers = userService.getAllUsers();

        // Then
        assertThat(allUsers).hasSize(2);
        assertThat(allUsers).containsExactlyInAnyOrder(createdUser1, createdUser2);
    }

    @Test
    @DisplayName("Should return correct user count")
    void shouldReturnCorrectUserCount() {
        // Given
        assertThat(userService.getUserCount()).isZero();

        // When
        userService.createUser(testUser1);
        userService.createUser(testUser2);

        // Then
        assertThat(userService.getUserCount()).isEqualTo(2);
    }

    @Test
    @DisplayName("Should check user existence correctly")
    void shouldCheckUserExistenceCorrectly() {
        // Given
        assertThat(userService.userExists(1L)).isFalse();

        // When
        UserDto createdUser = userService.createUser(testUser1);

        // Then
        assertThat(userService.userExists(createdUser.getId())).isTrue();
        assertThat(userService.userExists(999L)).isFalse();
    }

    @Test
    @DisplayName("Should handle concurrent user creation")
    void shouldHandleConcurrentUserCreation() throws InterruptedException {
        // Given
        int numberOfThreads = 10;
        Thread[] threads = new Thread[numberOfThreads];

        // When
        for (int i = 0; i < numberOfThreads; i++) {
            final int index = i;
            threads[i] = new Thread(() -> {
                UserDto user = new UserDto(null, "User" + index, "Test", "user" + index + "@test.com", 20 + index, "+1-555-000-" + String.format("%04d", index));
                userService.createUser(user);
            });
            threads[i].start();
        }

        // Wait for all threads to complete
        for (Thread thread : threads) {
            thread.join();
        }

        // Then
        assertThat(userService.getUserCount()).isEqualTo(numberOfThreads);
        
        // Verify all users have unique IDs
        List<UserDto> allUsers = userService.getAllUsers();
        assertThat(allUsers).extracting("id").doesNotHaveDuplicates();
    }
} 