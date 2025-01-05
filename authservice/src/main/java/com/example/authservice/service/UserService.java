package com.example.authservice.service;

import com.example.authservice.dto.RegisterRequestDTO;
import com.example.authservice.model.User;
import com.example.authservice.repository.UserRepository;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public void registerUser(RegisterRequestDTO registerRequest) {
        if (userRepository.existsByUsername(registerRequest.getUsername())) {
            throw new EntityExistsException("User already exists");
        }
        User user = new User();
        user.setUsername(registerRequest.getUsername());
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        userRepository.save(user);
    }


    public UserDetails loadUserByUsername(String username) throws EntityNotFoundException {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
    }

    public RegisterRequestDTO findByUsername(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
        return new RegisterRequestDTO(user.getUsername(), null);
    }
}
