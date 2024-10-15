package com.ref.cloudwirm.dto;

public class S3ObjectMetaData {
    private String fileName;
    private boolean isDir;
    private String folderPrefix;

    public S3ObjectMetaData(String fileName, boolean isDir, String folder) {
        this.fileName = fileName;
        this.isDir = isDir;
        this.folderPrefix = folder;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public boolean isDir() {
        return isDir;
    }

    public void setDir(boolean dir) {
        isDir = dir;
    }

    public String getFolderPrefix() {
        return folderPrefix;
    }

    public void setFolderPrefix(String folderPrefix) {
        this.folderPrefix = folderPrefix;
    }
}
