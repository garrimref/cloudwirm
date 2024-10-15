package com.ref.cloudwirm.service;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.containers.ComposeContainer;
import org.testcontainers.containers.DockerComposeContainer;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.io.File;

@Testcontainers
@ActiveProfiles("test")
public abstract class AbstractIntegrationTest {

    private static final ComposeContainer environment =
            new ComposeContainer(new File("docker-compose-test.yml"))
                    .withExposedService("mysql", 3306)
                    .withExposedService("minio", 9000);

    @BeforeAll
    public static void setUp() {
        environment.start();
    }

    @AfterAll
    public static void tearDown() {
        environment.stop();
    }
}
