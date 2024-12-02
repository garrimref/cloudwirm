package com.ref.cloudwirm.controller;

import com.ref.cloudwirm.dto.S3DeleteObjectRequest;
import com.ref.cloudwirm.dto.S3PersistFolderObjectRequest;
import com.ref.cloudwirm.dto.S3RenameObjectRequest;
import com.ref.cloudwirm.service.FolderStorageService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

@Controller
@RequestMapping("/folders")
public class FolderController {
    private final FolderStorageService folderStorageService;

    public FolderController(FolderStorageService folderStorageService) {
        this.folderStorageService = folderStorageService;
    }

    @PostMapping
    public RedirectView persistFolder(@Valid @ModelAttribute("folderPersistRequest") S3PersistFolderObjectRequest folderPersistRequest,
                                    BindingResult bindingResult,
                                    RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            throw new RuntimeException("Problem with folder persist binding");
        }
        folderStorageService.persistFolder(folderPersistRequest);
        // TODO: add a func to persist in specific folder

        redirectAttributes.addFlashAttribute("success", "Folder uploaded successfully");
        return new RedirectView("/");
    }

    @DeleteMapping
    public RedirectView deleteFolder(@Valid @ModelAttribute("objectDeleteRequest") S3DeleteObjectRequest deleteObjectRequest,
                                   BindingResult bindingResult,
                                   RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            throw new RuntimeException("Problem with folder delete binding");
        }
        folderStorageService.deleteFolder(deleteObjectRequest);

        redirectAttributes.addFlashAttribute("success", "Folder deleted successfully");
        return new RedirectView("/");
    }

    @PutMapping
    public RedirectView renameFolder(@Valid @ModelAttribute("renameRequest") S3RenameObjectRequest renameObjectRequest,
                                   BindingResult bindingResult,
                                   RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            throw new RuntimeException("Problem with folder rename binding");
        }
        folderStorageService.renameFolder(renameObjectRequest);

        redirectAttributes.addFlashAttribute("success", "Folder renamed successfully");
        return new RedirectView("/");
    }
}
