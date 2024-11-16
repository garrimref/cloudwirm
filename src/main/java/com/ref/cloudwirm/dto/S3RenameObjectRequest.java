package com.ref.cloudwirm.dto;

public class S3RenameObjectRequest extends S3Request{
    private String currentName;
    private String newName;

    public S3RenameObjectRequest() {
    }
    public S3RenameObjectRequest(Long ownerId, String currentName, String newName) {
        super(ownerId);
        this.currentName = currentName;
        this.newName = newName;
    }

    public String getCurrentName() {
        return currentName;
    }

    public void setCurrentName(String currentName) {
        this.currentName = currentName;
    }

    public String getNewName() {
        return newName;
    }

    public void setNewName(String newName) {
        this.newName = newName;
    }
}
