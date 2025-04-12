package com.backend.Backend.controllers;

import com.backend.Backend.security.SecurityData;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/pages")
public class PageController {
    final String ORIGIN = SecurityData.ORIGIN;

    @GetMapping("/message")
    public String showMessage(@RequestParam String message, Model model) {
        model.addAttribute("message", message);
        model.addAttribute("css", ORIGIN + "/css/messageCss.css");
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

        model.addAttribute("css", ORIGIN + "/css/resetPasswordCss.css");
        model.addAttribute("js", ORIGIN + "/js/resetPasswordJs.js");
        return "resetPassword";
    }
}
