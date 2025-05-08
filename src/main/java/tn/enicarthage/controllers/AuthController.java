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

    public static class LoginRequest {
        public String email;
        public String password;

    }

    @PostMapping("/login")
    public User login(@RequestBody LoginRequest request) {
        return authService.login(request.email, request.password);
    }
}
