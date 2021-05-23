package com.infoshare.controller;

import com.infoshare.formobjects.RegisterUserForm;
import com.infoshare.security.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
class RegistrationController {

    private final UserService userService;

    @Autowired
    public RegistrationController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/registration-submit")
    public String register(@ModelAttribute RegisterUserForm form) {
        userService.registerNewUser(form);
        return "redirect:/";
    }


    @GetMapping("/registration")
    public String registerForm(Model model) {
        model.addAttribute("form", new RegisterUserForm());
        return "register";
    }
}
