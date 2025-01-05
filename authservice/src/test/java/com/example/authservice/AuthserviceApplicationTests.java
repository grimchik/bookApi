package com.example.authservice;

import com.example.authservice.dto.RegisterRequestDTO;
import com.example.authservice.model.User;
import com.example.authservice.repository.UserRepository;
import com.example.authservice.service.UserService;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class AuthserviceApplicationTests {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void registerUser_ShouldSaveUser_WhenUserDoesNotExist() {
        RegisterRequestDTO request = new RegisterRequestDTO("testuser", "password123");
        when(userRepository.existsByUsername("testuser")).thenReturn(false);
        when(passwordEncoder.encode("password123")).thenReturn("encodedPassword");

        userService.registerUser(request);

        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void registerUser_ShouldThrowException_WhenUserAlreadyExists() {
        RegisterRequestDTO request = new RegisterRequestDTO("existingUser", "password123");
        when(userRepository.existsByUsername("existingUser")).thenReturn(true);

        EntityExistsException exception = assertThrows(EntityExistsException.class, () -> userService.registerUser(request));
        assertEquals("User already exists", exception.getMessage());
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void loadUserByUsername_ShouldReturnUserDetails_WhenUserExists() {
        User user = new User();
        user.setUsername("testuser");
        user.setPassword("encodedPassword");
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(user));

        UserDetails userDetails = userService.loadUserByUsername("testuser");

        assertNotNull(userDetails);
        assertEquals("testuser", userDetails.getUsername());
    }

    @Test
    void loadUserByUsername_ShouldThrowException_WhenUserNotFound() {
        when(userRepository.findByUsername("nonExistentUser")).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> userService.loadUserByUsername("nonExistentUser"));
        assertEquals("User not found", exception.getMessage());
    }

    @Test
    void findByUsername_ShouldReturnDTO_WhenUserExists() {
        User user = new User();
        user.setUsername("testuser");
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(user));

        RegisterRequestDTO result = userService.findByUsername("testuser");

        assertNotNull(result);
        assertEquals("testuser", result.getUsername());
        assertNull(result.getPassword());
    }

    @Test
    void findByUsername_ShouldThrowException_WhenUserNotFound() {
        when(userRepository.findByUsername("nonExistentUser")).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> userService.findByUsername("nonExistentUser"));
        assertEquals("User not found", exception.getMessage());
    }
}