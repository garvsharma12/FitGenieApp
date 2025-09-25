package com.draig.userservice.service;

import com.draig.userservice.Repository.UserRepository;
import com.draig.userservice.dto.RegisterRequest;
import com.draig.userservice.dto.UserResponse;
import com.draig.userservice.model.User;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Slf4j
public class UserService {

    private final UserRepository repository;
    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
    // Password hashing is handled in AuthController during registration/login

    public UserResponse register(RegisterRequest request) {

        if (repository.existsByEmail(request.getEmail())) {
            User existingUser = repository.findByEmail(request.getEmail());
            UserResponse userResponse = new UserResponse();
            userResponse.setId(existingUser.getId());
            userResponse.setUsername(existingUser.getUsername());
            userResponse.setPassword(existingUser.getPassword());
            userResponse.setEmail(existingUser.getEmail());
            userResponse.setFirstName(existingUser.getFirstName());
            userResponse.setLastName(existingUser.getLastName());
            userResponse.setCreatedAt(existingUser.getCreatedAt());
            userResponse.setUpdatedAt(existingUser.getUpdatedAt());
            return userResponse;
        }

        // Optional username uniqueness check
        if (request.getUsername() != null && !request.getUsername().isBlank()) {
            String desiredUsername = request.getUsername().trim();
            if (repository.existsByUsername(desiredUsername)) {
                throw new IllegalArgumentException("user already exist");
            }
            request.setUsername(desiredUsername);
        }

        User user = new User();
        user.setEmail(request.getEmail());
    user.setUsername(request.getUsername());
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        String raw = request.getPassword();
        if(raw != null && !raw.matches("^\\$2[aby]?\\$.*")){
            raw = encoder.encode(raw);
        }
        user.setPassword(raw);

        User savedUser = repository.save(user);
        UserResponse userResponse = new UserResponse();
        userResponse.setId(savedUser.getId());
        userResponse.setUsername(savedUser.getUsername());
        userResponse.setPassword(savedUser.getPassword());
        userResponse.setEmail(savedUser.getEmail());
        userResponse.setFirstName(savedUser.getFirstName());
        userResponse.setLastName(savedUser.getLastName());
        userResponse.setCreatedAt(savedUser.getCreatedAt());
        userResponse.setUpdatedAt(savedUser.getUpdatedAt());
        return userResponse;
    }

    public User findByEmail(String email){
        return repository.findByEmail(email);
    }

    public User findByUsername(String username){
        return repository.findByUsername(username);
    }

    public boolean existsByUsername(String username){
        return repository.existsByUsername(username);
    }

    public UserResponse getUserProfile(String userId) {
        User user = repository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        UserResponse userResponse = new UserResponse();
        userResponse.setId(user.getId());
    userResponse.setUsername(user.getUsername());
        userResponse.setPassword(user.getPassword());
        userResponse.setEmail(user.getEmail());
        userResponse.setFirstName(user.getFirstName());
        userResponse.setLastName(user.getLastName());
        userResponse.setCreatedAt(user.getCreatedAt());
        userResponse.setUpdatedAt(user.getUpdatedAt());
        return userResponse;

    }

    public Boolean existsById(String userId){
        return repository.existsById(userId);
    }

    public Boolean existByUserId(String userId){
        return repository.existsById(userId);
    }
}