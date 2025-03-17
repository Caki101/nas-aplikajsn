package com.backend.Backend.controllers;

import com.backend.Backend.comps.ConfirmationListener;
import com.backend.Backend.dataTypes.Tiket;
import com.backend.Backend.dataTypes.TravelDTO;
import com.backend.Backend.dataTypes.User;
import com.backend.Backend.repositories.KupljeniTiketiRepo;
import com.backend.Backend.repositories.TiketiRepo;
import com.backend.Backend.repositories.UsersRepo;
import com.backend.Backend.security.UserSecurity;
import com.backend.Backend.services.ConfirmationService;
import com.backend.Backend.services.MailgunService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private final MailgunService mailgunService;
    private final ConfirmationService confirmationService;
    private final ConfirmationListener confirmationListener;
    private final UsersRepo usersRepo;
    private final TiketiRepo tiketiRepo;
    private final KupljeniTiketiRepo kupljeniTiketiRepo;

    @Autowired
    public AuthController(MailgunService mailgunService,
                          ConfirmationService confirmationService,
                          ConfirmationListener confirmationListener,
                          UsersRepo usersRepo,
                          TiketiRepo tiketiRepo,
                          KupljeniTiketiRepo kupljeniTiketiRepo) {
        this.mailgunService = mailgunService;
        this.confirmationService = confirmationService;
        this.confirmationListener = confirmationListener;
        this.usersRepo = usersRepo;
        this.tiketiRepo = tiketiRepo;
        this.kupljeniTiketiRepo = kupljeniTiketiRepo;
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

    @GetMapping("/utc_{username}")
    public ResponseEntity<?> getUtc(@PathVariable String username) {
        if (usersRepo.findFirstByUsername(username) == null)
            return ResponseEntity.status(404).body("User not found.");

        return ResponseEntity.ok(tiketiRepo.findAllBought(username).size());
    }

    // in testing
    @PostMapping("/user_travels")
    public ResponseEntity<?> getUserTravels(@RequestBody User user) {
        List<TravelDTO> tdto = new ArrayList<>();

        List<Tiket> tiketi = tiketiRepo.findAllBought(user.getUsername());

        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm");

        tiketi.forEach(el -> tdto.add(new TravelDTO(sdf.format(el.getPolazak()), el.getSmestaj().getDrzava())));

        return ResponseEntity.ok(tdto);
    }

    // in testing
    @PostMapping("/check_identity")
    public ResponseEntity<?> resetPassword(@RequestBody User user) {
        if (user == null)
            return ResponseEntity.badRequest().body("Invalid request body.");
        if (user.getEmail() == null || user.getEmail().isEmpty())
            return ResponseEntity.badRequest().body("Invalid username.");
        if (user.getPassword() == null || user.getPassword().isEmpty())
            return ResponseEntity.badRequest().body("Invalid password.");

        User usr = usersRepo.findFirstByEmail(user.getEmail());

        if (usr == null)
            return ResponseEntity.status(404).body("User not found.");

        // session timeout implementation needed
        if (UserSecurity.verifyPassword(user.getPassword(), usr.getPassword()))
            return ResponseEntity.ok().body("Verified");
        else
            return ResponseEntity.status(401).body("Wrong password.");
    }
}