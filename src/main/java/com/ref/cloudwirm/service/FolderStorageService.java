package com.ref.cloudwirm.service;

import com.ref.cloudwirm.dto.S3ObjectMetaData;
import com.ref.cloudwirm.dto.S3PersistFolderObjectRequest;
import com.ref.cloudwirm.dto.S3DeleteObjectRequest;
import com.ref.cloudwirm.dto.S3RenameObjectRequest;
import io.minio.*;
import io.minio.messages.DeleteError;
import io.minio.messages.DeleteObject;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static com.ref.cloudwirm.utils.S3ObjectUtils.getUserRootFolderPrefix;

@Service
public class FolderStorageService {
    private final String bucket;
    private final MinioClient minioClient;
    private final FileStorageService fileStorageService;

    public FolderStorageService(MinioBucketService minioBucketService, MinioClient minioClient, FileStorageService fileStorageService) {
        this.bucket = minioBucketService.getBucket();
        this.minioClient = minioClient;
        this.fileStorageService = fileStorageService;
    }

    public void persistFolder(S3PersistFolderObjectRequest folder) {
        try {
            List<SnowballObject> objects = mapFilesToSnowball(folder.getOwnerId(), folder.getFiles());

            minioClient.uploadSnowballObjects(
                    UploadSnowballObjectsArgs.builder().bucket(bucket).objects(objects).build());
        } catch (Exception e) {
            throw new RuntimeException("Fail attempt to persist a Folder", e);
        }
    }

    public void deleteFolder(S3DeleteObjectRequest request) {
        Long ownerId = request.getOwnerId();
        List<S3ObjectMetaData> list = fileStorageService.getObjectsList(ownerId, request.getPath(),true);

        List<DeleteObject> objects = new LinkedList<>();
        for (S3ObjectMetaData file : list) {
            objects.add(new DeleteObject(getUserRootFolderPrefix(ownerId) + file.getFileName()));
        }

        Iterable<Result<DeleteError>> results =
                minioClient.removeObjects(
                        RemoveObjectsArgs.builder()
                                .bucket(bucket)
                                .objects(objects)
                                .build());

        for (Result<DeleteError> result : results) {
            try {
                DeleteError error = result.get();
                System.out.println(
                        "Error in deleting object " + error.objectName() + "; " + error.message());
            } catch (Exception e) {
                throw new RuntimeException("Error in deleting object", e);
            }
        }
    }

    public void renameFolder(S3RenameObjectRequest request) {
        List<S3ObjectMetaData> list = fileStorageService.getObjectsList(request.getOwnerId(), request.getCurrentName(), true);
        for (S3ObjectMetaData file : list) {
            fileStorageService.renameFile(
                    new S3RenameObjectRequest(
                            request.getOwnerId(),
                            file.getFileName(),
                            file.getFileName().replace(request.getCurrentName(), request.getNewName())
                    ));
        }

        deleteFolder(new S3DeleteObjectRequest(request.getOwnerId(), request.getCurrentName()));
    }

    private List<SnowballObject> mapFilesToSnowball(Long ownerId, List<MultipartFile> files) throws IOException {
        List<SnowballObject> snowballs = new ArrayList<>();
        for (MultipartFile f : files) {
            String originalFilename = f.getOriginalFilename();

            if (originalFilename == null || originalFilename.isBlank()) {
                continue;
            }

            snowballs.add(new SnowballObject(
                    getUserRootFolderPrefix(ownerId) + originalFilename,
                    f.getInputStream(),
                    f.getSize(),
                    null
            ));
        }
        return snowballs;
    }
}
