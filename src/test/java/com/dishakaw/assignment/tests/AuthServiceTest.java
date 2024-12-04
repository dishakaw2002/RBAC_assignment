package com.dishakaw.assignment.tests;

import com.dishakaw.assignment.model.User;
import com.dishakaw.assignment.service.AuthService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;
import java.util.HashSet;
import java.util.Set;

@SpringBootTest
public class AuthServiceTest {

    @Autowired
    private AuthService authService;

    private User testUser;

    @BeforeEach
    public void setup() {
        // Initialize roles with HashSet for flexibility
        Set<String> roles = new HashSet<>();
        roles.add("ROLE_USER");

        // Create user with roles
        testUser = new User("testuser", "password", roles);
    }

    @Test
    public void testRegisterUser() {
        // Simulate user registration
        ResponseEntity<?> response = authService.registerUser(testUser);

        // Print response body for debugging
        System.out.println("Response Body: " + response.getBody());

        // Check that the registration is successful
        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        assertThat(response.getBody()).toString().contains("User registered successfully");
    }

    @Test
    public void testLoginUser() {
        // First, register the user to ensure they exist in the database
        authService.registerUser(testUser);

        // Simulate user login
        ResponseEntity<?> response = authService.loginUser(testUser);

        // Check that the login is successful and JWT token is returned
        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        assertThat(response.getBody()).toString().contains("JWT Token");
    }
}
