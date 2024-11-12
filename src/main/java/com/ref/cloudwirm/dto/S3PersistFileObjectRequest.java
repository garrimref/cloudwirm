package com.ref.cloudwirm.dto;

import jakarta.validation.constraints.NotNull;
import org.springframework.web.multipart.MultipartFile;

public class S3PersistFileObjectRequest extends S3Request{

    @NotNull
    private MultipartFile file;

    public S3PersistFileObjectRequest(Long ownerId, MultipartFile file) {
        super(ownerId);
        this.file = file;
    }

    public S3PersistFileObjectRequest() {

    }

    public MultipartFile getFile() {
        return file;
    }
    public void setFile(MultipartFile file) {
        this.file = file;
    }
}
