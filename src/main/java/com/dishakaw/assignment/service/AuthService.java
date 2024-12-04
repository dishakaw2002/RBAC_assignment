package com.dishakaw.assignment.service;

import com.dishakaw.assignment.model.User;
import com.dishakaw.assignment.repository.AssignmentUserRepository;
import com.dishakaw.assignment.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class AuthService {

    @Autowired
    private AssignmentUserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    /**
     * Register a new user.
     *
     * @param user the user details
     * @return ResponseEntity with success or error message
     */
    public ResponseEntity<?> registerUser(User user) {
        // Check if user already exists
        if (userRepository.findByUsername(user.getUsername()) != null) {
            return ResponseEntity.badRequest().body("Error: Username already exists!");
        }

        // Hash the password before saving
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        // Set default role if not present
        if (user.getRoles() == null || user.getRoles().isEmpty()) {
            user.setRoles(Set.of("ROLE_USER"));
        }

        // Save the user to the database
        User savedUser = userRepository.save(user);
        return ResponseEntity.ok("User registered successfully with username: " + savedUser.getUsername());
    }

    /**
     * Login a user and generate a JWT.
     *
     * @param user the login details
     * @return ResponseEntity with JWT token or error message
     */
    public ResponseEntity<?> loginUser(User user) {
        // Find the user by username
        User foundUser = userRepository.findByUsername(user.getUsername());

        // Log the found user details (don't log the password!)
        System.out.println("Found user: " + (foundUser != null ? foundUser.getUsername() : "Not found"));

        if (foundUser == null) {
            return ResponseEntity.status(401).body("Error: Invalid username or password.");
        }

        // Validate the password
        boolean isPasswordValid = passwordEncoder.matches(user.getPassword(), foundUser.getPassword());
        System.out.println("Password valid: " + isPasswordValid);

        if (!passwordEncoder.matches(user.getPassword(), foundUser.getPassword())) {
            return ResponseEntity.status(401).body("Error: Invalid username or password.");
        }

        // Get roles from user (now it should work as the roles field is available)
        Set<String> roles = foundUser.getRoles(); // Make sure roles are being retrieved as Set

        // Generate JWT token
        String token = jwtUtil.generateToken(foundUser.getUsername(), roles); // Pass roles as a Set

        // Return the token
        return ResponseEntity.ok("JWT Token: " + token);
    }
}
