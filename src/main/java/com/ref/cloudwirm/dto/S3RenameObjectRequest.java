package com.ref.cloudwirm.dto;

public class S3RenameObjectRequest extends S3Request{
    private String path;
    private String newPath;

    public S3RenameObjectRequest() {
    }
    public S3RenameObjectRequest(Long ownerId, String path, String newPath) {
        super(ownerId);
        this.path = path;
        this.newPath = newPath;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getNewPath() {
        return newPath;
    }

    public void setNewPath(String newPath) {
        this.newPath = newPath;
    }
}
