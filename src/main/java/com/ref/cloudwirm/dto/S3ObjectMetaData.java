package com.ref.cloudwirm.dto;

import java.util.Objects;

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

    public S3ObjectMetaData(String path, String name, boolean isDir) {
        this.path = path;
        this.name = name;
        this.isDir = isDir;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        S3ObjectMetaData that = (S3ObjectMetaData) o;

        if (isDir != that.isDir) return false;
        if (!Objects.equals(path, that.path)) return false;
        if (!name.equals(that.name)) return false;
        return Objects.equals(folderPrefix, that.folderPrefix);
    }

    @Override
    public int hashCode() {
        int result = path != null ? path.hashCode() : 0;
        result = 31 * result + name.hashCode();
        result = 31 * result + (isDir ? 1 : 0);
        result = 31 * result + (folderPrefix != null ? folderPrefix.hashCode() : 0);
        return result;
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
