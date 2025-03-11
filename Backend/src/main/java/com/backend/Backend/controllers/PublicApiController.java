package com.backend.Backend.controllers;

import com.backend.Backend.services.ConfirmationService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/public-api")
public class PublicApiController {
    private final ConfirmationService confirmationService;

    @Autowired
    public PublicApiController(ConfirmationService confirmationService) {
        this.confirmationService = confirmationService;
    }

    @GetMapping("/verify_account/{code}")
    public void verifyToken(@PathVariable String code, HttpServletResponse response) throws IOException {
        boolean key_exist = confirmationService.confirmToken(code);
        String message;

        if (key_exist) message = "Request verified.";
        else message = "Request expired.";

        response.sendRedirect("/message/?message=" + message);
    }
}
