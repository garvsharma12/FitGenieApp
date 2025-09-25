package com.draig.userservice.Repository;

import com.draig.userservice.model.User;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, String> {

    boolean existsByEmail(@NotBlank(message = "Email is required") @Email(message = "Invalid Email format") String email);

    // ...existing code...

    User findByEmail(String email);

    boolean existsByUsername(String username);

    User findByUsername(String username);
}
