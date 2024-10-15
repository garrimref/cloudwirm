package com.ref.cloudwirm.dto;

public abstract class S3Request {
    private Long ownerId;

    public S3Request(Long ownerId) {
        this.ownerId = ownerId;
    }

    public Long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
    }
}
