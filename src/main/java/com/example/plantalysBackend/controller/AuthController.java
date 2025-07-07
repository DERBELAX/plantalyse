package com.example.plantalysBackend.controller;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import com.example.plantalysBackend.model.User;
import com.example.plantalysBackend.repository.UserRepository;
import com.example.plantalysBackend.security.JwtTokenUtil;
import com.example.plantalysBackend.security.UserDetailsImpl;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    /**
     * Inscription d'un nouvel utilisateur
     */
    @PostMapping("/signup")
    public User registerUser(@RequestBody User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRoles("USER"); 
        return userRepository.save(user);
    }

    /**
     * Connexion d'un utilisateur, retour du token JWT
     */
    @PostMapping("/login")
    public Map<String, String> login(@RequestBody Map<String, String> payload) {
        String email = payload.get("email");
        String password = payload.get("password");

        User user = userRepository.findByEmail(email);
        if (user == null || !passwordEncoder.matches(password, user.getPassword())) {
            throw new RuntimeException("Email ou mot de passe invalide");
        }

        System.out.println("Nom utilisateur: " + user.getFirstname()); 

        String token = jwtTokenUtil.generateToken(user);
        //String token = jwtTokenUtil.generateToken(email, role);
        System.out.println("Token généré: " + token); 

        return Map.of("token", token);
    }

    

    @GetMapping("/me")
    public User getCurrentUser(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        User user = userDetails.getUser();
        user.setPassword(null); 
        return user;
        
    }

}
