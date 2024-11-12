package com.ref.cloudwirm.utils;

import java.util.ArrayList;
import java.util.List;

public class BreadcrumbUtils {

    public static List<String> getBreadcrumbLinks(String path) {
        String[] parts = path.split("/");
        List<String> breadcrumbs = new ArrayList<>();

        StringBuilder currentPath = new StringBuilder();

        for (String part : parts) {
            if (part.isEmpty()) {
                continue;
            }
            currentPath.append(part).append("/");

            breadcrumbs.add(String.valueOf(currentPath));
        }

        return breadcrumbs;
    }

    public static List<String> getBreadcrumbNames(String path) {
        if (path.isEmpty()) {
            return List.of(path);
        }
        return List.of(path.split("/"));
    }
}
