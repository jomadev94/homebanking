package com.mindhub.homebanking.utils;


import com.mindhub.homebanking.exceptions.UnauthorizedException;
import com.mindhub.homebanking.models.Role;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class TokenUtils {

    private final static Long EXPIRED_MIN = 10L;
    @Value("${secret.token}")
    private String secret;

    public String generateToken(Authentication auth) {
        String email = auth.getName();
        Date expirationDate = new Date(System.currentTimeMillis() + EXPIRED_MIN * 1000 * 60);
        Map<String, Object> claims = new HashMap<>();
        var role = auth.getAuthorities().stream().findFirst().orElseThrow(UnauthorizedException::new).getAuthority();
        claims.put("role", role);
        claims.put("sub", email);
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date())
                .setExpiration(expirationDate)
                .signWith(this.getJwtKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public String generateToken(String email) {
        Date expirationDate = new Date(System.currentTimeMillis() + EXPIRED_MIN * 1000 * 60);
        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(new Date())
                .setExpiration(expirationDate)
                .signWith(this.getJwtKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public String getEmailFromJwt(String token) {
        Claims claims = Jwts.parserBuilder().setSigningKey(getJwtKey()).build().parseClaimsJws(token).getBody();
        return claims.getSubject();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(getJwtKey()).build().parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            throw new AuthenticationCredentialsNotFoundException("Jwt was expired or incorrect");
        }
    }

    private SecretKey getJwtKey() {
        byte[] keyBytes = this.secret.getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
