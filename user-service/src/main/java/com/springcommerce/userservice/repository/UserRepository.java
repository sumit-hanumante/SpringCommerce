package com.springcommerce.userservice.repository;

import com.springcommerce.userservice.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);

    // Method to find a user by their email
    Optional<User> findByEmail(String email);

    // Method to find a user by their password reset token
    Optional<User> findByResetToken(String resetToken);

    // Method to search for users by their name
    List<User> findByNameContaining(String query);
}
