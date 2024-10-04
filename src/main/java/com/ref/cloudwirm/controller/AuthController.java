package com.ref.cloudwirm.controller;

import com.ref.cloudwirm.domain.User;
import com.ref.cloudwirm.dto.UserDto;
import com.ref.cloudwirm.exception.UserAlreadyExistException;
import com.ref.cloudwirm.service.UserService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/user")
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/registration")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new UserDto());
        return "auth/registration";
    }

    @PostMapping("/registration")
    public String registerUser(@ModelAttribute("user") @Valid UserDto userDto,
                               BindingResult bindingResult,
                               Model model) {
        if (bindingResult.hasErrors()) {
            return "auth/registration";
        }
        try {
            userService.saveUser(userDto);
            return "redirect:/user/registration?success";
        } catch (UserAlreadyExistException uaeE) {
            model.addAttribute("errorMessage", uaeE.getMessage());
            return "auth/registration";
        }
    }

    @GetMapping("/login")
    public String login() {
        return "auth/login";
    }
}
