package com.springcommerce.userservice.service;

import com.springcommerce.userservice.model.User;
import com.springcommerce.userservice.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserService {
    private UserRepository userRepository;
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public List<User> findAllUsers() {
        return userRepository.findAll();
    }

    public User createUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    public Optional<User> updateUser(String username, User newUser) {
        return userRepository.findByUsername(username)
                .map(user -> {
                    user.setEmail(newUser.getEmail());
                    user.setPassword(passwordEncoder.encode(newUser.getPassword()));
                    return userRepository.save(user);
                });
    }

    public void deleteUser(String username) {
        userRepository.findByUsername(username)
                .ifPresent(userRepository::delete);
    }

    public User registerUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    public void resetPassword(String email) {
        // TODO: Implement error handling for when the email is not found in the database
        Optional<User> userOptional = userRepository.findByEmail(email);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            // Generate a random password reset token
            String resetToken = UUID.randomUUID().toString();
            user.setResetToken(resetToken);
            userRepository.save(user);
            // TODO: Implement an email service to send the reset token to the user's email
            // You could use a service like JavaMailSender or a third-party service like SendGrid
            // Make sure to include a link in the email that directs the user to a password reset page
            // The link should include the reset token as a parameter
        }
    }


    public void verifyUser(String token) {
        Optional<User> userOptional = userRepository.findByResetToken(token);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            user.setVerified(true);
            userRepository.save(user);
        }
    }

    public List<User> searchUsers(String query) {
        return userRepository.findByNameContaining(query);
    }
}
