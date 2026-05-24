package com.example.kanban.auth;

import com.example.kanban.auth.dto.LoginRequest;
import com.example.kanban.auth.dto.LoginResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:5173")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(
        @RequestBody LoginRequest request
    ) {
        try {
            LoginResponse response = authService.login(request);
            return ResponseEntity.ok(response);
            // returns: { token, email, name }
        } catch (RuntimeException e) {
            return ResponseEntity
                .status(401)
                .body("Invalid credentials");
        }
    }
}