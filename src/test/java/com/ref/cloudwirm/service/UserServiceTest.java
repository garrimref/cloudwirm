package com.ref.cloudwirm.service;

import com.ref.cloudwirm.dto.UserDto;
import com.ref.cloudwirm.exception.UserAlreadyExistException;
import com.ref.cloudwirm.repos.UserRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Testcontainers
@ActiveProfiles("dev")
public class UserServiceTest {
    private static final DockerImageName MINIO_IMAGE = DockerImageName.parse("quay.io/minio/minio");
    @Container
    static MySQLContainer<?> mySQLContainer = new MySQLContainer<>("mysql:latest")
            .withDatabaseName("cloudwirm_db")
            .withUsername("root")
            .withPassword("admin");

    @DynamicPropertySource
    static void registerMySQLProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", mySQLContainer::getJdbcUrl);
        registry.add("spring.datasource.username", mySQLContainer::getUsername);
        registry.add("spring.datasource.password", mySQLContainer::getPassword);
    }

    private static final GenericContainer<?> minio = new GenericContainer<>(MINIO_IMAGE)
            .withExposedPorts(9000, 9001)
            .withEnv("MINIO_ROOT_USER", "testuser")
            .withEnv("MINIO_ROOT_PASSWORD", "testpassword")
            .withCommand("server /data --console-address :9001");
    @DynamicPropertySource
    static void minioProperties(DynamicPropertyRegistry registry) {
        registry.add("minio.endpoint", () -> "http://" + minio.getHost() + ":" + minio.getMappedPort(9000));
        registry.add("minio.accessKey", () -> "testuser");
        registry.add("minio.secretKey", () -> "testpassword");
        registry.add("minio.bucket", () -> "user-files");
    }

    @BeforeAll
    static void ensureContainerIsRunning() {
        if (!minio.isRunning()) {
            minio.start();
        }
    }

    /*@AfterAll
    static void stopContainer() {
        if (minio.isRunning()) {
            minio.stop();
        }
        if (mySQLContainer.isRunning()) {
            mySQLContainer.stop();
        }
    }*/

    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;


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