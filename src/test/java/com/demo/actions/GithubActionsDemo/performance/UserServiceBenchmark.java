package com.demo.actions.GithubActionsDemo.performance;

import com.demo.actions.GithubActionsDemo.dto.UserDto;
import com.demo.actions.GithubActionsDemo.service.UserService;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;

import java.util.concurrent.TimeUnit;

/**
 * JMH Benchmark for UserService operations.
 * 
 * <p>This benchmark demonstrates performance testing capabilities
 * and can be used to detect performance regressions.
 */
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MICROSECONDS)
@State(Scope.Benchmark)
@Fork(value = 1, warmups = 1)
@Warmup(iterations = 3, time = 1)
@Measurement(iterations = 5, time = 1)
public class UserServiceBenchmark {

    private UserService userService;
    private UserDto testUser;

    @Setup
    public void setup() {
        userService = new UserService();
        testUser = new UserDto(null, "John", "Doe", "john.doe@example.com", 30, "+1-555-123-4567");
    }

    @Benchmark
    public void createUserBenchmark(Blackhole blackhole) {
        UserDto createdUser = userService.createUser(testUser);
        blackhole.consume(createdUser);
    }

    @Benchmark
    public void getUserByIdBenchmark(Blackhole blackhole) {
        // Create a user first
        UserDto createdUser = userService.createUser(testUser);
        // Then retrieve it
        UserDto retrievedUser = userService.getUserById(createdUser.getId());
        blackhole.consume(retrievedUser);
    }

    @Benchmark
    public void getAllUsersBenchmark(Blackhole blackhole) {
        // Create multiple users
        for (int i = 0; i < 10; i++) {
            UserDto user = new UserDto(null, "User" + i, "Test", 
                    "user" + i + "@test.com", 20 + i, "+1-555-000-" + String.format("%04d", i));
            userService.createUser(user);
        }
        // Then get all users
        var allUsers = userService.getAllUsers();
        blackhole.consume(allUsers);
    }

    @Benchmark
    public void updateUserBenchmark(Blackhole blackhole) {
        // Create a user first
        UserDto createdUser = userService.createUser(testUser);
        // Then update it
        UserDto updateData = new UserDto(null, "John", "Updated", 
                "john.updated@example.com", 31, "+1-555-123-4567");
        UserDto updatedUser = userService.updateUser(createdUser.getId(), updateData);
        blackhole.consume(updatedUser);
    }

    @Benchmark
    public void deleteUserBenchmark(Blackhole blackhole) {
        // Create a user first
        UserDto createdUser = userService.createUser(testUser);
        // Then delete it
        userService.deleteUser(createdUser.getId());
        blackhole.consume(createdUser.getId());
    }

    @Benchmark
    public void userExistsBenchmark(Blackhole blackhole) {
        // Create a user first
        UserDto createdUser = userService.createUser(testUser);
        // Then check if it exists
        boolean exists = userService.userExists(createdUser.getId());
        blackhole.consume(exists);
    }

    @Benchmark
    public void getUserCountBenchmark(Blackhole blackhole) {
        // Create multiple users
        for (int i = 0; i < 5; i++) {
            UserDto user = new UserDto(null, "User" + i, "Test", 
                    "user" + i + "@test.com", 20 + i, "+1-555-000-" + String.format("%04d", i));
            userService.createUser(user);
        }
        // Then get count
        long count = userService.getUserCount();
        blackhole.consume(count);
    }
} 