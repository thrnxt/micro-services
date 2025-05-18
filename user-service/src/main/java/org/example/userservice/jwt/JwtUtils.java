package org.example.userservice.jwt;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtUtils {
    private final Key key;
    private final long expirationMs;

    public JwtUtils( @Value("${jwt.expiration-ms}") long expirationMs, @Value("${jwt.secret}") String base64secret){
        this.expirationMs = expirationMs;
        byte[] keyBytes = Decoders.BASE64.decode(base64secret);
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    public String generateToken(String username){
        Date now = new Date();
        Date expiry = new Date(now.getTime() + expirationMs);
        return Jwts.builder()
                .subject(username)
                .issuedAt(now)
                .expiration(expiry)
                .signWith(key)
                .compact();
    }

    public String getUsernameFromToken(String token) {
        return Jwts.parser()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    public boolean validateToken(String token){
        try {
            Jwts.parser().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        }
        catch (JwtException | IllegalArgumentException e){
            return false;
        }
    }
}
