package com.ref.cloudwirm.dto;

public class S3DownloadObjectRequest extends S3Request {

    private String path;

    private String name;

    public S3DownloadObjectRequest() {
    }

    public S3DownloadObjectRequest(Long ownerId, String path, String name) {
        super(ownerId);
        this.path = path;
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
