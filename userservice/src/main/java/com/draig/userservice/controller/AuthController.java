package com.draig.userservice.controller;

import com.draig.userservice.dto.RegisterRequest;
import com.draig.userservice.model.User;
import com.draig.userservice.security.JwtService;
import com.draig.userservice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final UserService userService;
    private final JwtService jwtService;
    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody @jakarta.validation.Valid RegisterRequest request){
        // Hash password
        request.setPassword(encoder.encode(request.getPassword()));
        try {
            return ResponseEntity.ok(userService.register(request));
        } catch (IllegalArgumentException ex){
            return ResponseEntity.status(409).body(Map.of("error", ex.getMessage()));
        } catch (DataIntegrityViolationException ex){
            return ResponseEntity.status(409).body(Map.of("error", "user already exist"));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String,String> payload){
        String identifier = payload.getOrDefault("identifier", payload.get("email"));
        String password = payload.get("password");
        if(identifier == null || password == null){
            return ResponseEntity.badRequest().body(Map.of("error", "identifier and password are required"));
        }
        User user;
        if(identifier.contains("@")){
            user = userService.findByEmail(identifier);
        } else {
            user = userService.findByUsername(identifier);
        }
        if(user == null || !encoder.matches(password, user.getPassword())){
            return ResponseEntity.status(401).body(Map.of(
                    "error", "Invalid credentials",
                    "message", "The email/username or password you entered is incorrect.",
                    "code", "AUTH_INVALID_CREDENTIALS"
            ));
        }
        String token = jwtService.generateToken(user.getId());
        return ResponseEntity.ok(Map.of("token", token));
    }
}
