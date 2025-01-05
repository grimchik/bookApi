package com.example.authservice.controller;

import com.example.authservice.dto.RegisterRequestDTO;
import com.example.authservice.service.UserService;
import com.example.authservice.util.JwtTokenUtil;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private UserService userService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @PostMapping("/register")
    public ResponseEntity<String> register(@Valid @RequestBody RegisterRequestDTO registerRequest) {
        userService.registerUser(registerRequest);
        return new ResponseEntity<>("User registered successfully", HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@Valid @RequestBody RegisterRequestDTO loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword())
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        return new ResponseEntity<>(jwtTokenUtil.generateToken(loginRequest.getUsername()), HttpStatus.OK);
    }
}