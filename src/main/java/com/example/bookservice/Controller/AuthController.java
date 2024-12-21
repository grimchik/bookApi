package com.example.bookservice.controller;

import com.example.bookservice.token.JwtTokenUtil;
import org.springframework.web.bind.annotation.*;

@RestController
public class AuthController {

    private static final String VALID_USERNAME = "user";
    private static final String VALID_PASSWORD = "password";

    @PostMapping ("/authenticate")
    public String authenticate(@RequestParam String username, @RequestParam String password) {

        if (VALID_USERNAME.equals(username) && VALID_PASSWORD.equals(password)) {
            return JwtTokenUtil.generateToken(username);
        } else {
            return "Invalid credentials";
        }
    }
}
