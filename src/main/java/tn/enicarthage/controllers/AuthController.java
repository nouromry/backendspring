package tn.enicarthage.controllers;

import tn.enicarthage.models.User;
import tn.enicarthage.services.AuthService;
import org.springframework.web.bind.annotation.*;

@RestController

@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    // Create a DTO class to hold login credentials
    public static class LoginRequest {
        public String email;
        public String password;

        // Getters and Setters (optional if using public fields)
    }

    @PostMapping("/login")
    public User login(@RequestBody LoginRequest request) {
        return authService.login(request.email, request.password);
    }
}
