package com.ref.cloudwirm.service;

import com.ref.cloudwirm.dto.*;
import com.ref.cloudwirm.utils.S3ObjectUtils;
import io.minio.*;
import io.minio.errors.*;
import io.minio.messages.Item;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
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
        if (file.getOriginalFilename().isBlank()) {
            throw new RuntimeException("File need to be named");
        }
        try {
            String fileName = S3ObjectUtils.getUserRootFolderPrefix(object.getOwnerId()) + file.getOriginalFilename();
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

    public ByteArrayResource downloadFile(S3DownloadObjectRequest request) {
        try (InputStream stream = minioClient.getObject(
                GetObjectArgs.builder()
                        .bucket(bucket)
                        .object(S3ObjectUtils.getUserRootFolderPrefix(request.getOwnerId()) + request.getPath())
                        .build())) {
            return new ByteArrayResource(stream.readAllBytes());
        }
        catch (Exception e) {
            throw new RuntimeException("There is an error while downloading the file, try again later");
        }
    }

    public void renameFile(S3RenameObjectRequest request) {
        try {
            minioClient.copyObject(
                    CopyObjectArgs.builder()
                            .bucket(bucket)
                            .object(S3ObjectUtils.getUserRootFolderPrefix(request.getOwnerId()) + request.getNewPath())
                            .source(CopySource.builder()
                                            .bucket(bucket)
                                            .object(S3ObjectUtils.getUserRootFolderPrefix(request.getOwnerId())
                                                    + request.getPath())
                                            .build())
                            .build());

            S3DeleteObjectRequest removeObjectRequest = new S3DeleteObjectRequest(request.getOwnerId(), request.getPath());
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
                String path = S3ObjectUtils.removeUserRootFolderPrefix(item.objectName(), userId);
                String name = S3ObjectUtils.getNameFromPath(path);
                s3objects.add(new S3ObjectMetaData(
                        path,
                        name,
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
