package com.demo.actions.GithubActionsDemo.service;

import com.demo.actions.GithubActionsDemo.dto.UserDto;
import com.demo.actions.GithubActionsDemo.exception.UserNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Service class for managing user operations.
 * 
 * <p>This service provides business logic for user management operations.
 * It uses an in-memory storage for demonstration purposes.
 */
@Service
public class UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);
    
    private final Map<Long, UserDto> users = new ConcurrentHashMap<>();
    private final AtomicLong idCounter = new AtomicLong(1);

    /**
     * Get all users.
     *
     * @return list of all users
     */
    public List<UserDto> getAllUsers() {
        logger.debug("Retrieving all users");
        return new ArrayList<>(users.values());
    }

    /**
     * Get user by ID.
     *
     * @param id the user ID
     * @return the user
     * @throws UserNotFoundException if user is not found
     */
    public UserDto getUserById(Long id) {
        logger.debug("Retrieving user with ID: {}", id);
        UserDto user = users.get(id);
        if (user == null) {
            logger.warn("User not found with ID: {}", id);
            throw new UserNotFoundException("User not found with ID: " + id);
        }
        return user;
    }

    /**
     * Create a new user.
     *
     * @param userDto the user to create
     * @return the created user
     */
    public UserDto createUser(UserDto userDto) {
        logger.debug("Creating new user: {}", userDto.getEmail());
        
        // Check if user with same email already exists
        boolean emailExists = users.values().stream()
                .anyMatch(user -> user.getEmail().equals(userDto.getEmail()));
        
        if (emailExists) {
            logger.warn("User with email {} already exists", userDto.getEmail());
            throw new IllegalArgumentException("User with email " + userDto.getEmail() + " already exists");
        }

        // Generate new ID and create user
        Long newId = idCounter.getAndIncrement();
        userDto.setId(newId);
        users.put(newId, userDto);
        
        logger.info("Created user with ID: {}", newId);
        return userDto;
    }

    /**
     * Update an existing user.
     *
     * @param id the user ID
     * @param userDto the updated user data
     * @return the updated user
     * @throws UserNotFoundException if user is not found
     */
    public UserDto updateUser(Long id, UserDto userDto) {
        logger.debug("Updating user with ID: {}", id);
        
        if (!users.containsKey(id)) {
            logger.warn("User not found with ID: {}", id);
            throw new UserNotFoundException("User not found with ID: " + id);
        }

        // Check if email is being changed and if it conflicts with existing user
        UserDto existingUser = users.get(id);
        if (!existingUser.getEmail().equals(userDto.getEmail())) {
            boolean emailExists = users.values().stream()
                    .anyMatch(user -> user.getEmail().equals(userDto.getEmail()) && !user.getId().equals(id));
            
            if (emailExists) {
                logger.warn("User with email {} already exists", userDto.getEmail());
                throw new IllegalArgumentException("User with email " + userDto.getEmail() + " already exists");
            }
        }

        userDto.setId(id);
        users.put(id, userDto);
        
        logger.info("Updated user with ID: {}", id);
        return userDto;
    }

    /**
     * Delete a user.
     *
     * @param id the user ID
     * @throws UserNotFoundException if user is not found
     */
    public void deleteUser(Long id) {
        logger.debug("Deleting user with ID: {}", id);
        
        if (!users.containsKey(id)) {
            logger.warn("User not found with ID: {}", id);
            throw new UserNotFoundException("User not found with ID: " + id);
        }

        users.remove(id);
        logger.info("Deleted user with ID: {}", id);
    }

    /**
     * Get user count.
     *
     * @return the number of users
     */
    public long getUserCount() {
        return users.size();
    }

    /**
     * Check if user exists.
     *
     * @param id the user ID
     * @return true if user exists, false otherwise
     */
    public boolean userExists(Long id) {
        return users.containsKey(id);
    }
} 