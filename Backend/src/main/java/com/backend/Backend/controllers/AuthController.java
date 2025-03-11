package com.backend.Backend.controllers;

import com.backend.Backend.comps.ConfirmationListener;
import com.backend.Backend.dataTypes.User;
import com.backend.Backend.services.ConfirmationService;
import com.backend.Backend.services.MailgunService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private final MailgunService mailgunService;
    private final ConfirmationService confirmationService;
    private final ConfirmationListener confirmationListener;

    @Autowired
    public AuthController(MailgunService mailgunService,
                          ConfirmationService confirmationService,
                          ConfirmationListener confirmationListener) {
        this.mailgunService = mailgunService;
        this.confirmationService = confirmationService;
        this.confirmationListener = confirmationListener;
    }

    // testing jwt token
    @PutMapping("/change_username")
    public ResponseEntity<?> changeUsername(@RequestBody User user) {
        return ResponseEntity.ok().build();
    }

    // needs improvement
    @PostMapping("/change_password")
    public ResponseEntity<?> changePassword(@RequestBody User user) {
        String token;

        if (!user.getEmail().isEmpty() && user.getEmail() != null) {
            token = confirmationService.generateToken();
        }
        else return ResponseEntity.badRequest().build();

        mailgunService.sendEmail(
                user.getEmail(),
                "Change password requested",
                "<div><a href='http://26.10.184.197:8080/public/verify/"+token+"'>Confirm</a></div>");

        boolean confirmed = confirmationListener.waitForConfirmation(token, 60_000);

        if (confirmed) return ResponseEntity.ok("Confirmed");

        return ResponseEntity.badRequest().body("Timed out");
    }
}
