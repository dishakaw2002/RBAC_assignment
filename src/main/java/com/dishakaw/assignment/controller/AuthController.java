package com.dishakaw.assignment.controller;

import com.dishakaw.assignment.model.User;
import com.dishakaw.assignment.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    // Endpoint for user registration
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody User user) {
        return authService.registerUser(user);
    }

    // Endpoint for user login
    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody User user) {
        return authService.loginUser(user);
    }

    // Endpoint for user logout (Optional)
    @PostMapping("/logout")
    public ResponseEntity<?> logoutUser() {
        // You can clear tokens on the client side; server logic can vary
        return ResponseEntity.ok("Logged out successfully");
    }

    // Secured endpoint that requires a valid JWT
    @GetMapping("/secure-endpoint")
    public ResponseEntity<String> secureEndpoint() {
        return ResponseEntity.ok("You have access to this secure endpoint!");
    }
}
