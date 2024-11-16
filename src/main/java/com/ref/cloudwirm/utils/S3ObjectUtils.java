package com.ref.cloudwirm.utils;

public class S3ObjectUtils {

    public static String getUserRootFolderPrefix(Long userId) {
        return "user-" + userId + "-files/";
    }

    public static String removeUserRootFolderPrefix(String path, Long userId) {
        return path.replace("user-" + userId + "-files/", "");
    }

    public static String getNameFromPath(String path) {
        if (path.endsWith("/")) {
            path = path.substring(0, path.length() - 1);
        }

        int lastSlashIndex = path.lastIndexOf("/");

        if (lastSlashIndex == -1) {
            return path;
        }
        return path.substring(lastSlashIndex + 1);
    }
}
