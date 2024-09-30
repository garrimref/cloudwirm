package com.ref.cloudwirm.service;

import com.ref.cloudwirm.dto.UserDto;
import com.ref.cloudwirm.exception.UserAlreadyExistException;
import com.ref.cloudwirm.repos.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
@Testcontainers
public class UserServiceTest {

    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;

    @Container
    static MySQLContainer<?> mySQLContainer = new MySQLContainer<>("mysql:latest")
            .withDatabaseName("cloudwirm_testdb")
            .withUsername("testuser")
            .withPassword("testpassword");

    @DynamicPropertySource
    static void registerMySQLProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", mySQLContainer::getJdbcUrl);
        registry.add("spring.datasource.username", mySQLContainer::getUsername);
        registry.add("spring.datasource.password", mySQLContainer::getPassword);
    }

    @BeforeEach
    void clearTable() {
        userRepository.deleteAll();
    }
    @Test
    void testSuccessfulUserRegistration() {
        String name = "testuser";
        String password = "password123";
        userService.saveUser(new UserDto(name,password));

        assertEquals(1, userRepository.findAll().size());
        assertEquals(name, userRepository.findByUsername(name).getUsername());
        assertNotEquals(password, userRepository.findByUsername(name).getPassword(), "password should be hashed");
    }

    @Test
    void testUserRegistrationWithDuplicateUsername() {
        String name = "username";
        String password = "pass123";
        userService.saveUser(new UserDto(name,password));

        assertThrows(UserAlreadyExistException.class,
                () -> userService.saveUser(new UserDto(name,password)));
    }

    @Test
    void testUserRegistrationWithNullObject() {
        UserDto user = null;

        assertThrows(NullPointerException.class,
                () -> userService.saveUser(user));
    }
}