package com.api.modules.auth.security;

import java.nio.charset.StandardCharsets;
import java.util.Date;

import org.springframework.stereotype.Component;

import com.api.modules.user.model.User;

import io.github.cdimascio.dotenv.Dotenv;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtUtils {

    private final String jwtSecret;
    private final long jwtExpirationMs;

    public JwtUtils() {
        Dotenv dotenv = Dotenv.load();
        this.jwtSecret = dotenv.get("JWT_SECRET");
        this.jwtExpirationMs = Long.parseLong(dotenv.get("JWT_EXPIRATION_MS"));
    }

    // Generar token JWT
    public String generateToken(User user) {
        return Jwts.builder()
                .setSubject(user.getId().toString()) 
                .claim("email", user.getEmail()) 
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpirationMs))
                .signWith(Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8)))
                .compact();
    }

    // Obtener claims
    public Claims getClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8)))
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    // Obtener userId
    public String getUserIdFromToken(String token) {
        return getClaims(token).getSubject();
    }

    // // Obtener email
    // public String getEmailFromToken(String token) {
    //     return getClaims(token).get("email", String.class);
    // }

    // Validar token
    public boolean validateToken(String token) {
        try {
            getClaims(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}