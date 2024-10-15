package com.ref.cloudwirm.controller;

import com.ref.cloudwirm.domain.User;
import com.ref.cloudwirm.service.FileStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class HomeController {

    @GetMapping("/")
    public String index(@AuthenticationPrincipal User user,
                        @RequestParam(value = "path", required = false) String path,
                        Model model) {
        model.addAttribute("user", user);

        model.addAttribute("files");
        return "index";
    }
}
