package com.ref.cloudwirm.controller;

import com.ref.cloudwirm.dto.S3PersistFileObjectRequest;
import com.ref.cloudwirm.service.FileStorageService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

@Controller
@RequestMapping("/files")
public class FileController {

    private final FileStorageService fileService;

    public FileController(FileStorageService fileService) {
        this.fileService = fileService;
    }

    @PostMapping
    public RedirectView persistFile(@Valid @ModelAttribute("filePersistRequest") S3PersistFileObjectRequest filePersistRequest,
                                    BindingResult bindingResult,
                                    RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            throw new RuntimeException("Problem with filepersist binding");
        }
        fileService.persistFile(filePersistRequest);

        redirectAttributes.addFlashAttribute("success", "File uploaded successfully");
        return new RedirectView("/");
    }
}
