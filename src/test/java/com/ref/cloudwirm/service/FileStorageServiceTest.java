package com.ref.cloudwirm.service;

import com.ref.cloudwirm.dto.S3DeleteObjectRequest;
import com.ref.cloudwirm.dto.S3ObjectMetaData;
import com.ref.cloudwirm.dto.S3PersistFileObjectRequest;
import com.ref.cloudwirm.dto.S3RenameObjectRequest;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@Testcontainers
@ActiveProfiles("test")
public class FileStorageServiceTest {
    private static final DockerImageName MINIO_IMAGE = DockerImageName.parse("quay.io/minio/minio");

    @Container
    static MySQLContainer<?> mySQLContainer = new MySQLContainer<>("mysql:latest")
            .withDatabaseName("cloudwirm_testdb")
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
            .withCommand("server /data");
    @DynamicPropertySource
    static void minioProperties(DynamicPropertyRegistry registry) {
        registry.add("minio.endpoint", () -> "http://" + minio.getHost() + ":" + minio.getMappedPort(9000));
        registry.add("minio.accessKey", () -> "testuser");
        registry.add("minio.secretKey", () -> "testpassword");
        registry.add("minio.bucket", () -> "user-files");
    }

    @Autowired
    private FileStorageService fileStorageService;

    @BeforeAll
    static void ensureContainerIsRunning() {
        if (!minio.isRunning()) {
            minio.start();
        }
    }
    @Test
    void putFileObject() {
        String objectName = "file-1.txt";
        Long ownerId = 1L;
        MockMultipartFile mockFile = getMockMultipartFile(objectName);


        S3PersistFileObjectRequest testObject = new S3PersistFileObjectRequest(ownerId, mockFile);

        fileStorageService.persistFile(testObject);
        List<S3ObjectMetaData> s3FileObjects = fileStorageService.getObjectsList(ownerId, "", false);

        assertEquals(1, s3FileObjects.size());
        assertEquals(objectName, s3FileObjects.get(0).getPath());
    }

    @Test
    void renameFileObject() {
        String oldName = "oldName.txt";
        String newName = "newName.txt";
        Long ownerId = 1L;

        MockMultipartFile mockFile = getMockMultipartFile(oldName);
        fileStorageService.persistFile(new S3PersistFileObjectRequest(ownerId, mockFile));

        fileStorageService.renameFile(new S3RenameObjectRequest(ownerId, oldName, newName));
        List<S3ObjectMetaData> files = fileStorageService.getObjectsList(ownerId, "", false);

        Long countWithOldName = files.stream()
                .filter(f -> f.getPath().equals(oldName))
                .count();
        Long countWithNewName = files.stream()
                .filter(f -> f.getPath().equals(newName))
                .count();

        assertEquals(0, countWithOldName);
        assertEquals(1, countWithNewName);
    }

    @Test
    void deleteFileObject() {
        String fileName = "deleteObject.txt";
        Long ownerId = 1L;

        MockMultipartFile mockFile = getMockMultipartFile(fileName);
        fileStorageService.persistFile(new S3PersistFileObjectRequest(ownerId, mockFile));

        List<S3ObjectMetaData> filesBeforeDelete = fileStorageService.getObjectsList(ownerId, "", false);

        Long countBeforeDelete = filesBeforeDelete.stream()
                .filter(f -> f.getPath().equals(fileName))
                .count();

        assertEquals(1, countBeforeDelete);

        fileStorageService.deleteFile(new S3DeleteObjectRequest(ownerId, fileName));

        List<S3ObjectMetaData> filesAfterDelete = fileStorageService.getObjectsList(ownerId, "", false);

        Long countAfterDelete = filesAfterDelete.stream()
                .filter(f -> f.getPath().equals(fileName))
                .count();

        assertEquals(0, countAfterDelete);
    }

    @NotNull
    private MockMultipartFile getMockMultipartFile(String originalFilename) {
        return new MockMultipartFile(
                "files",
                originalFilename,
                "text/plain",
                "content".getBytes()
        );
    }
}
