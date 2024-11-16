package com.ref.cloudwirm.dto;

public class S3ObjectMetaData {
    private String path;
    private String name;
    private boolean isDir;
    private String folderPrefix;

    public S3ObjectMetaData(String path, String name, boolean isDir, String folder) {
        this.path = path;
        this.name = name;
        this.isDir = isDir;
        this.folderPrefix = folder;
    }

    public String getName() {
        return name;
    }
    public void setName(String name){
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
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
