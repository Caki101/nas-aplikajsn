package com.backend.Backend.controllers;

import com.backend.Backend.services.ConfirmationService;
import com.backend.Backend.systemFiling.StorageService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/public-api")
public class PublicApiController {
    private final ConfirmationService confirmationService;
    private final StorageService storageService;

    @Autowired
    public PublicApiController(ConfirmationService confirmationService,
                               StorageService storageService) {
        this.confirmationService = confirmationService;
        this.storageService = storageService;
    }

    @GetMapping("/verify_account/{code}")
    public void verifyToken(@PathVariable String code, HttpServletResponse response) throws IOException {
        boolean key_exist = confirmationService.confirmToken(code);
        String message;

        if (key_exist) message = "Request verified.";
        else message = "Request expired.";

        response.sendRedirect("/pages/message?message=" + message);
    }

    @GetMapping("/first-image/{smestaj_id}")
    public ResponseEntity<?> firstImage(@PathVariable Long smestaj_id) {
        Resource img = storageService.loadFirst(smestaj_id);

        if (img == null) return ResponseEntity.notFound().build();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_PNG);

        return new ResponseEntity<>(img, headers, HttpStatus.OK);
    }

    @GetMapping("/image/{filename}")
    @ResponseBody
    public ResponseEntity<?> image(@PathVariable String filename) {
        Resource img = storageService.load(filename);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_PNG);

        return new ResponseEntity<>(img, headers, HttpStatus.OK);
    }
}
