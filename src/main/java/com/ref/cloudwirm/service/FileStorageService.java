package com.ref.cloudwirm.service;

import com.ref.cloudwirm.dto.S3PersistFileObjectRequest;
import com.ref.cloudwirm.dto.S3ObjectMetaData;
import com.ref.cloudwirm.dto.S3DeleteObjectRequest;
import com.ref.cloudwirm.dto.S3RenameObjectRequest;
import com.ref.cloudwirm.utils.S3ObjectUtils;
import io.minio.*;
import io.minio.messages.Item;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Service
public class FileStorageService  {

    private final String bucket;
    private final MinioClient minioClient;

    public FileStorageService(MinioBucketService minioBucketService, MinioClient minioClient) {
        bucket = minioBucketService.getBucket();
        this.minioClient = minioClient;
    }
    public void persistFile(S3PersistFileObjectRequest object) {
        MultipartFile file = object.getFile();
        if (file.getName().isBlank()) {
            throw new RuntimeException("File need to be named");
        }
        try {
            String fileName = S3ObjectUtils.getUserRootFolderPrefix(object.getOwnerId()) + file.getName();
            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(bucket)
                            .object(fileName)
                            .stream(file.getInputStream(), file.getSize(), -1)
                            .build());
        } catch (Exception e) {
            throw new RuntimeException("Failed to put an Object", e);
        }
    }

    public void deleteFile(S3DeleteObjectRequest request) {
        try {
            minioClient.removeObject(
                    RemoveObjectArgs.builder()
                            .bucket(bucket)
                            .object(S3ObjectUtils.getUserRootFolderPrefix(request.getOwnerId())
                                    + request.getPath())
                            .build());
        } catch (Exception e) {
            throw new RuntimeException("Failed to delete an Object", e);
        }
    }

    public void renameFile(S3RenameObjectRequest request) {
        try {
            minioClient.copyObject(
                    CopyObjectArgs.builder()
                            .bucket(bucket)
                            .object(S3ObjectUtils.getUserRootFolderPrefix(request.getOwnerId()) + request.getNewName())
                            .source(CopySource.builder()
                                            .bucket(bucket)
                                            .object(S3ObjectUtils.getUserRootFolderPrefix(request.getOwnerId())
                                                    + request.getCurrentName())
                                            .build())
                            .build());

            S3DeleteObjectRequest removeObjectRequest = new S3DeleteObjectRequest(request.getOwnerId(), request.getCurrentName());
            deleteFile(removeObjectRequest);
        } catch (Exception e) {
            throw new RuntimeException("Failed to rename an Object", e);
        }
    }


    public List<S3ObjectMetaData> getObjectsList(Long userId, String folder, boolean isRecursive) {
        Iterable<Result<Item>> results = minioClient.listObjects(
                ListObjectsArgs.builder()
                        .bucket(bucket)
                        .prefix(S3ObjectUtils.getUserRootFolderPrefix(userId) + folder)
                        .recursive(isRecursive)
                        .build());

        List<S3ObjectMetaData> s3objects = new ArrayList<>();
        results.forEach(result -> {
            try {
                Item item = result.get();
                s3objects.add(new S3ObjectMetaData(
                        S3ObjectUtils.removeUserRootFolderPrefix(item.objectName(), userId),
                        item.isDir(),
                        folder));
            }
            catch (Exception e) {
                throw new RuntimeException("Failed to get objects list", e);
            }
        });
        return s3objects;
    }
}