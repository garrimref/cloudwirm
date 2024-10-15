package com.ref.cloudwirm.dto;

import jakarta.validation.constraints.NotEmpty;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public class S3PersistFolderObjectRequest extends S3Request {

    @NotEmpty
    private List<MultipartFile> files;

    public S3PersistFolderObjectRequest(Long ownerId, List<MultipartFile> files) {
        super(ownerId);
        this.files = files;
    }

    public List<MultipartFile> getFiles() {
        return files;
    }

    public void setFiles(List<MultipartFile> files) {
        this.files = files;
    }
}
