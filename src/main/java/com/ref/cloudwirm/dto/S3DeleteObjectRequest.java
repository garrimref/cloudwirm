package com.ref.cloudwirm.dto;

public class S3DeleteObjectRequest extends S3Request {
    private String path;

    public S3DeleteObjectRequest() {

    }
    public S3DeleteObjectRequest(Long ownerId, String path) {
        super(ownerId);
        this.path = path;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
