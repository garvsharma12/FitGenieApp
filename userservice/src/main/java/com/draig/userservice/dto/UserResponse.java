package com.draig.userservice.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UserResponse {
    private String id;
    private String username;
    // ...existing code...
    private String email;
    private String password;
    private String fullName;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
