package com.tms.tms_backend.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class JwtService {

    // =====================================
    // SECRET KEY
    // =====================================

    private static final String SECRET_KEY =
            "mySuperSecureJwtSecretKeyForTravelManagementSystem123";

    // =====================================
    // GET SIGN KEY
    // =====================================

    private Key getSignKey() {

        return Keys.hmacShaKeyFor(
                SECRET_KEY.getBytes(StandardCharsets.UTF_8)
        );
    }

    // =====================================
    // GENERATE TOKEN
    // =====================================

    public String generateToken(
            String email,
            String role,
            Long userId
    ) {

        Map<String, Object> claims =
                new HashMap<>();

        claims.put("role", role);

        claims.put("userId", userId);

        return Jwts.builder()

                .setClaims(claims)

                .setSubject(email)

                .setIssuedAt(new Date())

                .setExpiration(
                        new Date(
                                System.currentTimeMillis()
                                        + 1000 * 60 * 60 * 24
                        )
                )

                .signWith(
                        getSignKey(),
                        SignatureAlgorithm.HS256
                )

                .compact();
    }

    // =====================================
    // EXTRACT EMAIL
    // =====================================

    public String extractEmail(
            String token
    ) {

        return extractAllClaims(token)
                .getSubject();
    }

    // =====================================
    // EXTRACT ROLE
    // =====================================

    public String extractRole(
            String token
    ) {

        return extractAllClaims(token)
                .get("role", String.class);
    }

    // =====================================
    // EXTRACT USER ID
    // =====================================

    public Long extractUserId(
            String token
    ) {

        // Claim may deserialize as Integer or Long depending on the JSON parser.
        Number id =
                extractAllClaims(token)
                        .get("userId", Number.class);

        if (id == null) {
            return null;
        }

        return id.longValue();
    }

    // =====================================
    // VALIDATE TOKEN
    // =====================================

    public boolean isTokenValid(
            String token,
            String email
    ) {

        String extractedEmail =
                extractEmail(token);

        return extractedEmail.equals(email)
                &&
                !isTokenExpired(token);
    }

    // =====================================
    // TOKEN EXPIRY
    // =====================================

    private boolean isTokenExpired(
            String token
    ) {

        return extractAllClaims(token)
                .getExpiration()
                .before(new Date());
    }

    // =====================================
    // ALL CLAIMS
    // =====================================

    private Claims extractAllClaims(
            String token
    ) {

        return Jwts.parser()

                .setSigningKey(getSignKey())

                .parseClaimsJws(token)

                .getBody();
    }
}
