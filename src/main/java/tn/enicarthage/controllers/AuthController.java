package tn.enicarthage.controllers;

import tn.enicarthage.models.User;
import tn.enicarthage.services.AuthService;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin("*") 
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public User login(@RequestParam String email, @RequestParam String password) {
        return authService.login(email, password);
    }
}
