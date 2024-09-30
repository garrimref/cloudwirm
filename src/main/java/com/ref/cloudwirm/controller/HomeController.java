package com.ref.cloudwirm.controller;

import com.ref.cloudwirm.domain.User;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/")
    public String index(@AuthenticationPrincipal User user, Model model) {
        model.addAttribute("user", user);
        return "index";
    }
}
