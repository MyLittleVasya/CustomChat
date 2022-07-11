package com.chat.custom.customchat.controllers;

import com.chat.custom.customchat.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Controller
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/registration")
    public String getRegistrationForm(Model model) {
        return "registration";
    }

    @PostMapping("/registration")
    public String registration(@RequestParam Map<String, String> form, Model model) {
       model.addAttribute("message", userService.tryRegistrateUser(form));
       return "registration";
    }

    @GetMapping("/activate/{uuid}")
    public String activateAccount(@PathVariable String uuid, Model model) {
        model.addAttribute("message", userService.tryActivateUser(uuid));
        return "login";
    }
}
