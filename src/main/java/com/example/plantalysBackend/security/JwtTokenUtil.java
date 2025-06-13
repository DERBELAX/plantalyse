package com.example.plantalysBackend.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

@Component
public class JwtTokenUtil {
	@Value("${jwt.secret}")
    private String jwtSecret;

    @Value("${jwt.expiration}")
    private long jwtExpirationMs;

    public String generateToken(String email, List<String> roles) {
        return Jwts.builder()
            .setSubject(email)
            .claim("roles", roles)
            .setIssuedAt(new Date())
            .setExpiration(new Date(System.currentTimeMillis() + jwtExpirationMs))
            .signWith(SignatureAlgorithm.HS512, jwtSecret)
            .compact();
    }

    public String getEmail(String token) {
        return Jwts.parser()
        		.setSigningKey(jwtSecret)
        		.parseClaimsJws(token)
        		.getBody()
        		.getSubject();
    }

    public List<String> getRoles(String token) {
        return (List<String>) Jwts
        		.parser()
        		.setSigningKey(jwtSecret)
        		.parseClaimsJws(token)
        		.getBody()
        		.get("roles");
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

}
