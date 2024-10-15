package com.ref.cloudwirm.controller;

import com.ref.cloudwirm.service.FileStorageService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Controller
public class ObjectStorageController {

    private final FileStorageService OSService;

    public ObjectStorageController(FileStorageService OSService) {
        this.OSService = OSService;
    }

    @PostMapping
    public String putObject(@RequestParam MultipartFile file, Model model){
        try {
            //OSService.persistObject();
        } catch (Exception e) {
            throw new RuntimeException("Failed to put an Object in bucket", e);
        }
        return "redirect:/";
    }

}
