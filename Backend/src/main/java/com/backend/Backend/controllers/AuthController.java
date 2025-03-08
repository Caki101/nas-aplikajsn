package com.backend.Backend.controllers;

import com.backend.Backend.dataTypes.User;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    // testing jwt token
    @PutMapping("/change_username")
    public ResponseEntity<?> changeUsername(@RequestBody User user) {
        return ResponseEntity.ok().build();
    }
}
