package com.draig.userservice.dto;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RegisterRequest {
    @NotBlank(message = "Email is required")
    @Email(message = "Invalid Email format")
    private String email;

    @NotBlank(message = "Password is Required")
    @Size(min=6, message = "password must be of atleast 6 characters")
    private String password;

    // keycloakId removed

    @NotBlank(message = "Username is required")
    private String username;
    private String fullName;

}
