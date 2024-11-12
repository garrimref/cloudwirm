package com.ref.cloudwirm.utils;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BreadcrumbUtilsTest {

    @Test
    void getBreadcrumbElements_whenHaveBlankPath_thenHave0Size() {
        String path = "";
        var breadcrumbs = BreadcrumbUtils.getBreadcrumbLinks(path);

        long size = breadcrumbs.size();
        assertEquals(0, size);
    }

    @Test
    void getBreadcrumbElement_whenRegularPath_thenCorrect() {
        String path = "seria/s1/";
        var breadcrumbs = BreadcrumbUtils.getBreadcrumbLinks(path);

        assertEquals(2, breadcrumbs.size());
        assertEquals(1, breadcrumbs.stream()
                .filter(f -> f.equals("seria/"))
                .count());
        assertEquals(1, breadcrumbs.stream()
                .filter(f -> f.equals("seria/s1/"))
                .count());
    }

    @Test
    void getFoldersNames_whenHaveBlankPath_thenHave0Size() {
        String path = "";
        var breadcrumbs = BreadcrumbUtils.getBreadcrumbLinks(path);

        long size = breadcrumbs.size();
        assertEquals(0, size);
    }
    @Test
    void getFoldersNames_whenRegularPath_thenCorrect() {
        String path = "seria/s1/";
        var foldersNames = BreadcrumbUtils.getBreadcrumbNames(path);

        assertEquals(2, foldersNames.size());
        assertEquals(1, foldersNames.stream()
                .filter(f -> f.equals("seria"))
                .count());
        assertEquals(1, foldersNames.stream()
                .filter(f -> f.equals("s1"))
                .count());
    }
}