package com.ref.cloudwirm.service;

import io.minio.BucketExistsArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import io.minio.errors.MinioException;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class MinioBucketService {

    @Value("${minio.bucket}")
    private String bucket;

    private final MinioClient minioClient;

    public MinioBucketService(MinioClient minioClient) {
        this.minioClient = minioClient;
    }

    @PostConstruct
    public void ensureBucketExists() {
        try {
            boolean isBucketExists = minioClient.bucketExists(
                    BucketExistsArgs.builder()
                            .bucket(bucket)
                            .build());
            if (!isBucketExists) {
                minioClient.makeBucket(
                        MakeBucketArgs.builder()
                                .bucket(bucket)
                                .build());
            }
        } catch (MinioException e){
            System.err.println("Error occurred while checking or creating bucket: " + e);
        } catch (Exception e) {
            throw new RuntimeException("Failed to create bucket in MinIO", e);
        }
    }

    public String getBucket() {
        return bucket;
    }
}
