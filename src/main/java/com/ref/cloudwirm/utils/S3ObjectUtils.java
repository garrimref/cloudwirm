package com.ref.cloudwirm.utils;

public class S3ObjectUtils {

    public static String getUserRootFolderPrefix(Long userId) {
        return "user-" + userId + "-files/";
    }

    public static String removeUserRootFolderPrefix(String path, Long userId) {
        return path.replace("user-" + userId + "-files/", "");
    }

    public static String getFileNameFromPath(String path) {
        StringBuilder fileName = new StringBuilder();
        if (path.endsWith("/")) {

        }
        return "";
    }
}
