package com.ref.cloudwirm.service;

import com.ref.cloudwirm.dto.S3DeleteObjectRequest;
import com.ref.cloudwirm.dto.S3ObjectMetaData;
import com.ref.cloudwirm.dto.S3PersistFolderObjectRequest;
import com.ref.cloudwirm.dto.S3RenameObjectRequest;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@ActiveProfiles("test")
public class FolderStorageServiceTest extends AbstractIntegrationTest {

    @Autowired
    private FolderStorageService folderStorageService;
    @Autowired
    private FileStorageService fileStorageService;


    @Test
    void persistFolder() {
        Long ownerId = 1L;

        MockMultipartFile file = getMockMultipartFile("sub/file.txt");

        List<MultipartFile> folder = new ArrayList<>();
        folder.add(file);

        folderStorageService.persistFolder(new S3PersistFolderObjectRequest(ownerId, folder));

        List<S3ObjectMetaData> files = fileStorageService.getObjectsList(ownerId, "", false);

        long folders = files.stream()
                .filter(S3ObjectMetaData::isDir)
                .filter(f -> f.getFileName().equals("sub/"))
                .count();
        assertEquals(1, folders);

        List<S3ObjectMetaData> filesInSubFolder = fileStorageService.getObjectsList(ownerId, "sub/", false);
        assertEquals(1, filesInSubFolder.size());
        assertEquals("sub/file.txt", filesInSubFolder.get(0).getFileName());
    }

    @Test
    void renameFolder() {
        Long ownerId = 1L;

        MockMultipartFile file = getMockMultipartFile("subBeforeRename/file.txt");

        List<MultipartFile> folder = new ArrayList<>();
        folder.add(file);

        folderStorageService.persistFolder(new S3PersistFolderObjectRequest(ownerId, folder));
        folderStorageService.renameFolder(new S3RenameObjectRequest(ownerId, "subBeforeRename", "subAfterRename"));

        List<S3ObjectMetaData> files = fileStorageService.getObjectsList(ownerId, "", false);

        assertEquals(0, files.stream()
                .filter(S3ObjectMetaData::isDir)
                .filter(f -> f.getFileName().equals("subBeforeRename/"))
                .count());
        assertEquals(1, files.stream()
                .filter(S3ObjectMetaData::isDir)
                .filter(f -> f.getFileName().equals("subAfterRename/"))
                .count());

        List<S3ObjectMetaData> filesInSubFolder = fileStorageService.getObjectsList(ownerId, "subAfterRename/", false);
        assertEquals(1, filesInSubFolder.size());
        assertEquals("subAfterRename/file.txt", filesInSubFolder.get(0).getFileName());
    }

    @Test
    void deleteFolder() {
        Long ownerId = 1L;

        MockMultipartFile file = getMockMultipartFile("subToDelete/file.txt");

        List<MultipartFile> folder = new ArrayList<>();
        folder.add(file);

        folderStorageService.persistFolder(new S3PersistFolderObjectRequest(ownerId, folder));

        List<S3ObjectMetaData> files = fileStorageService.getObjectsList(ownerId, "", false);
        assertEquals(1, files.stream()
                .filter(S3ObjectMetaData::isDir)
                .filter(f -> f.getFileName().equals("subToDelete/"))
                .count());

        folderStorageService.deleteFolder(new S3DeleteObjectRequest(ownerId, "subToDelete/"));
        List<S3ObjectMetaData> files2 = fileStorageService.getObjectsList(ownerId, "", false);
        assertEquals(0, files2.stream()
                .filter(S3ObjectMetaData::isDir)
                .filter(f -> f.getFileName().equals("subToDelete/"))
                .count());
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
