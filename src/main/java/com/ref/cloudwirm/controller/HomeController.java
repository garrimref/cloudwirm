package com.ref.cloudwirm.controller;

import com.ref.cloudwirm.domain.User;
import com.ref.cloudwirm.repos.UserRepository;
import com.ref.cloudwirm.service.FileStorageService;
import com.ref.cloudwirm.service.FolderStorageService;
import com.ref.cloudwirm.service.UserService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class HomeController {
    private final FileStorageService fileStorageService;

    private final FolderStorageService folderStorageService;

    private final UserRepository userRepository;
    public HomeController(FileStorageService fileStorageService, FolderStorageService folderStorageService, UserRepository userRepository) {
        this.fileStorageService = fileStorageService;
        this.folderStorageService = folderStorageService;
        this.userRepository = userRepository;
    }

    @GetMapping("/")
    public String index(@AuthenticationPrincipal UserDetails userDetails,
                        @RequestParam(value = "path", required = false) String path,
                        Model model) {
        User user = userRepository.findByUsername(userDetails.getUsername());
        model.addAttribute("username", user.getUsername());
        model.addAttribute("path", path);
        model.addAttribute("files", fileStorageService.getObjectsList(user.getId(), path, false));

        return "index";
    }
}
