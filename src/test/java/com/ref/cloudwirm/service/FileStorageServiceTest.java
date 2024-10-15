package com.ref.cloudwirm.service;

import com.ref.cloudwirm.dto.S3DeleteObjectRequest;
import com.ref.cloudwirm.dto.S3ObjectMetaData;
import com.ref.cloudwirm.dto.S3PersistFileObjectRequest;
import com.ref.cloudwirm.dto.S3RenameObjectRequest;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@ActiveProfiles("test")
public class FileStorageServiceTest extends AbstractIntegrationTest {


    @Autowired
    private FileStorageService fileStorageService;

    @Test
    void putFileObject() {
        String objectName = "file-1.txt";
        Long ownerId = 1L;
        MockMultipartFile mockFile = getMockMultipartFile(objectName);


        S3PersistFileObjectRequest testObject = new S3PersistFileObjectRequest(ownerId, mockFile);

        fileStorageService.persistFile(testObject);
        List<S3ObjectMetaData> s3FileObjects = fileStorageService.getObjectsList(ownerId, "", false);

        assertEquals(1, s3FileObjects.size());
        assertEquals(objectName, s3FileObjects.get(0).getFileName());
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
                .filter(f -> f.getFileName().equals(oldName))
                .count();
        Long countWithNewName = files.stream()
                .filter(f -> f.getFileName().equals(newName))
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
                .filter(f -> f.getFileName().equals(fileName))
                .count();

        assertEquals(1, countBeforeDelete);

        fileStorageService.deleteFile(new S3DeleteObjectRequest(ownerId, fileName));

        List<S3ObjectMetaData> filesAfterDelete = fileStorageService.getObjectsList(ownerId, "", false);

        Long countAfterDelete = filesAfterDelete.stream()
                .filter(f -> f.getFileName().equals(fileName))
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
