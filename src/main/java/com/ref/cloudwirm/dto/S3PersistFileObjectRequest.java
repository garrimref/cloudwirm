package com.ref.cloudwirm.dto;

import jakarta.validation.constraints.NotNull;
import org.springframework.web.multipart.MultipartFile;

public class S3PersistFileObjectRequest extends S3Request{

    @NotNull
    private final MultipartFile file;

    public S3PersistFileObjectRequest(Long ownerId, MultipartFile file) {
        super(ownerId);
        this.file = file;
    }


    public MultipartFile getFile() {
        return file;
    }
}
