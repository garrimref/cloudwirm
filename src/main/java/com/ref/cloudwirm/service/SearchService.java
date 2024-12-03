package com.ref.cloudwirm.service;

import com.ref.cloudwirm.dto.S3ObjectMetaData;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class SearchService {
    private final FileStorageService fileStorageService;

    public SearchService(FileStorageService fileStorageService) {
        this.fileStorageService = fileStorageService;
    }

    public List<S3ObjectMetaData> search(Long ownerId, String query) {
        if (ownerId == null || query == null) {
            throw new IllegalArgumentException("Owner id or search query cannot be empty");
        }
        List<S3ObjectMetaData> objectsList = fileStorageService.getObjectsList(ownerId, "", true);

        List<S3ObjectMetaData> foundFiles = objectsList.stream()
                .filter(file -> file.getName().toLowerCase().contains(query.toLowerCase()))
                .toList();

        Set<S3ObjectMetaData> foundFolders = findMatchingFolders(objectsList, query);

        List<S3ObjectMetaData> results = new ArrayList<>();

        results.addAll(foundFiles);
        results.addAll(foundFolders);

        return results;
    }

    private static Set<S3ObjectMetaData> findMatchingFolders(List<S3ObjectMetaData> objects, String query) {
        objects = removeFileNamesFromPath(objects);

        Set<S3ObjectMetaData> matchingFolders = new HashSet<>();

        for (S3ObjectMetaData folder : objects) {
            String path = folder.getPath();
            String[] parts = path.split("/");

            StringBuilder currentPath = new StringBuilder();

            for (String part : parts) {
                currentPath.append(part).append("/");
                if (part.toLowerCase().contains(query.toLowerCase())) {
                    matchingFolders.add(new S3ObjectMetaData(
                            folder.getPath(),
                            part,
                            true
                    ));
                }
            }
        }

        return matchingFolders;
    }

    private static List<S3ObjectMetaData> removeFileNamesFromPath(List<S3ObjectMetaData> files) {
        return files.stream()
                .peek(f -> {
                    String path = f.getPath();
                    String name = f.getName();

                    f.setPath(path.replace(name, ""));
                })
                .toList();
    }
}
