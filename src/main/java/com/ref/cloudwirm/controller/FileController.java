package com.ref.cloudwirm.controller;

import com.ref.cloudwirm.dto.S3DeleteObjectRequest;
import com.ref.cloudwirm.dto.S3DownloadObjectRequest;
import com.ref.cloudwirm.dto.S3PersistFileObjectRequest;
import com.ref.cloudwirm.dto.S3RenameObjectRequest;
import com.ref.cloudwirm.service.FileStorageService;
import jakarta.validation.Valid;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
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
            throw new RuntimeException("Problem with file persist binding");
        }
        fileService.persistFile(filePersistRequest);
        // TODO: add a func to persist in specific folder

        redirectAttributes.addFlashAttribute("success", "File uploaded successfully");
        return new RedirectView("/");

    }

    @GetMapping(produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    @ResponseBody
    public ResponseEntity<ByteArrayResource> downloadFile(
            @Valid @ModelAttribute("downloadRequest") S3DownloadObjectRequest downloadRequest,
            BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            throw new RuntimeException("Problem with file download binding");
        }
        ByteArrayResource file = fileService.downloadFile(downloadRequest);

        return ResponseEntity.ok()
                .header("Content-Disposition", "attachment; filename=\"" + downloadRequest.getName() + "\"")
                .body(file);
    }


    @DeleteMapping
    public RedirectView deleteFile(@Valid @ModelAttribute("deleteRequest")S3DeleteObjectRequest deleteObjectRequest,
                                   BindingResult bindingResult,
                                   RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            throw new RuntimeException("Problem with file delete binding");
        }
        fileService.deleteFile(deleteObjectRequest);

        redirectAttributes.addFlashAttribute("success", "File deleted successfully");
        return new RedirectView("/");
    }

    @PutMapping
    public RedirectView renameFile(@Valid @ModelAttribute("renameRequest") S3RenameObjectRequest renameObjectRequest,
                                   BindingResult bindingResult,
                                   RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            throw new RuntimeException("Problem with file rename binding");
        }
        fileService.renameFile(renameObjectRequest);

        redirectAttributes.addFlashAttribute("success", "File renamed successfully");
        return new RedirectView("/");
    }
}
