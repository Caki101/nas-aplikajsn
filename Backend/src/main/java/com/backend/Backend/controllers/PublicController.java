package com.backend.Backend.controllers;

import com.backend.Backend.services.ConfirmationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/public")
public class PublicController {
    private final ConfirmationService confirmationService;

    @Autowired
    public PublicController(ConfirmationService confirmationService) {
        this.confirmationService = confirmationService;
    }

    // in testing
    @GetMapping("/verify/{code}")
    public void verifyToken(@PathVariable String code) {
        confirmationService.confirmToken(code);
    }
}
