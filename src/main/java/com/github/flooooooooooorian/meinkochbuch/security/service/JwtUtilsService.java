package com.github.flooooooooooorian.meinkochbuch.security.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.Map;

@Service
public class JwtUtilsService {
    @Value("${jwt.secret:}")
    private String secret;

    public String createToken(Map<String, Object> claims, String subject) {

        return Jwts.builder()
                .addClaims(claims)
                .setSubject(subject)
                .setIssuedAt(Date.from(Instant.now()))
                .setExpiration(Date.from(Instant.now().plus(Duration.ofHours(3))))
                .signWith(SignatureAlgorithm.HS256, secret)
                .compact();
    }

    public Claims parseClaim(String token) {
        return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
    }
}
