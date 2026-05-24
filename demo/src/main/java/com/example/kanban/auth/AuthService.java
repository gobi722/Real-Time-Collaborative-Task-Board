package com.example.kanban.auth;

import com.example.kanban.auth.dto.LoginRequest;
import com.example.kanban.auth.dto.LoginResponse;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final JwtUtil jwtUtil;

    public AuthService(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    public LoginResponse login(LoginRequest request) {

        // For now — hardcoded user
        // Later → check DB for real user
        if (request.getEmail().equals("admin@kanban.com") &&
            request.getPassword().equals("password123")) {

            String token = jwtUtil.generateToken(request.getEmail());

            return new LoginResponse(
                token,
                request.getEmail(),
                "Admin User"
            );
        }

        throw new RuntimeException("Invalid credentials");
    }
}