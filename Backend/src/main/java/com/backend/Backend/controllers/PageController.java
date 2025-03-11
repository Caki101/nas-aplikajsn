package com.backend.Backend.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/pages")
public class PageController {

    @GetMapping("/message")
    public String showMessage(@RequestParam String message, Model model) {
        model.addAttribute("message", message);
        return "message";
    }

    @GetMapping("/reset_password/{token}")
    public String resetPassword(@PathVariable String token,
                                @RequestParam String email,
                                Model model) {
        if (email.isEmpty()) {
            model.addAttribute("error", "No email supplied in request.");
            return "error";
        }

        return "resetPassword";
    }
}
