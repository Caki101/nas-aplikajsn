package com.backend.Backend.controllers;

import com.backend.Backend.comps.ConfirmationListener;
import com.backend.Backend.dataTypes.User;
import com.backend.Backend.repositories.UsersRepo;
import com.backend.Backend.security.UserSecurity;
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
    private final UsersRepo usersRepo;

    @Autowired
    public AuthController(MailgunService mailgunService,
                          ConfirmationService confirmationService,
                          ConfirmationListener confirmationListener,
                          UsersRepo usersRepo) {
        this.mailgunService = mailgunService;
        this.confirmationService = confirmationService;
        this.confirmationListener = confirmationListener;
        this.usersRepo = usersRepo;
    }

    @PostMapping("/change_password")
    public ResponseEntity<?> changePassword(@RequestBody User user) {
        if (!user.getEmail().isEmpty() && user.getEmail() != null) {
            if (!user.getPassword().isEmpty() && user.getPassword() != null) {
                User usr = usersRepo.findFirstByEmail(user.getEmail());

                String password = UserSecurity.encryptPassword(user.getPassword());

                usr.setPassword(password);

                usersRepo.save(usr);

                return ResponseEntity.ok().build();
            }
            else return ResponseEntity.badRequest().body("Invalid password.");
        }
        else return ResponseEntity.badRequest().body("Invalid email.");
    }
}
