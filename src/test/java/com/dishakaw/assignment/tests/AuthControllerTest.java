package com.dishakaw.assignment.tests;

import com.dishakaw.assignment.controller.AuthController;
import com.dishakaw.assignment.model.User;
import com.dishakaw.assignment.service.AuthService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.HashSet;
import java.util.Set;

@SpringBootTest
public class AuthControllerTest {

    @Autowired
    private AuthController authController;

    @Autowired
    private AuthService authService;

    private String jwtToken;

    @BeforeEach
    public void setup() {
        Set<String> roles = new HashSet<>();
        roles.add("ROLE_USER");
        User testUser = new User("testuser", "password", roles);

        authService.registerUser(testUser); // Register user
        ResponseEntity<?> loginResponse = authService.loginUser(testUser);
        jwtToken = (String) loginResponse.getBody(); // Extract JWT from login response
    }

    @Test
public void testSecureEndpointWithValidToken() {
    RestTemplate restTemplate = new RestTemplate();
    String url = "http://localhost:8080/api/auth/secure-endpoint"; // Correct URL
    // Make sure that port is 8080 and the app is running
    HttpHeaders headers = new HttpHeaders();
    headers.add("Authorization", "Bearer " + jwtToken);
    ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, new HttpEntity<>(headers), String.class);
    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
}


    @Test
    public void testSecureEndpointWithInvalidToken() {
        // Create HTTP headers with an invalid Authorization Bearer token
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer invalid_token");

        // Simulate sending a GET request to the secure endpoint
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.exchange(
                "http://localhost:8080/api/auth/secure-endpoint", HttpMethod.GET, new HttpEntity<>(headers),
                String.class);

        // Verify that the response is 403 Forbidden
        assertThat(response.getStatusCodeValue()).isEqualTo(403);
    }

}
