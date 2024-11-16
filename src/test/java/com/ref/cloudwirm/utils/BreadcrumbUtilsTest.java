package com.ref.cloudwirm.utils;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class BreadcrumbUtilsTest {

    @Test
    void getBreadcrumbLinks_whenHaveBlankPath_thenHave0Size() {
        String path = "";
        var breadcrumbs = BreadcrumbUtils.getBreadcrumbLinks(path);

        long size = breadcrumbs.size();
        assertEquals(0, size);
    }

    @Test
    void getBreadcrumbLinks_whenRegularPath_thenCorrect() {
        String path = "seria/s1/";
        List<String> expected = List.of("seria/", "seria/s1/");
        var breadcrumbs = BreadcrumbUtils.getBreadcrumbLinks(path);

        assertEquals(2, breadcrumbs.size());
        assertLinesMatch(expected, breadcrumbs);
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
        List<String> expected = List.of("seria", "s1");
        var foldersNames = BreadcrumbUtils.getBreadcrumbNames(path);

        assertEquals(2, foldersNames.size());
        assertLinesMatch(expected, foldersNames);
    }
}