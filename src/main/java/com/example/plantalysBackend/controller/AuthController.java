package com.example.plantalysBackend.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.plantalysBackend.model.User;
import com.example.plantalysBackend.repository.UserRepository;
import com.example.plantalysBackend.security.JwtTokenUtil;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
	@Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @PostMapping("/login")
    public Map<String, String> login(@RequestBody Map<String, String> payload) {
        String email = payload.get("email");
        String password = payload.get("password");

        User user = userRepository.findByEmail(email);
        if (user == null || !passwordEncoder.matches(password, user.getPassword())) {
            throw new RuntimeException("Email ou mot de passe invalide");
        }

        List<String> roles = List.of(user.getRoles().split(","));
        String token = jwtTokenUtil.generateToken(email, roles);

        return Map.of("token", token);
    }

}
