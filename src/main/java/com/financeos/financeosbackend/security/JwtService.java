package com.financeos.financeosbackend.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Service;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import java.util.Date;

@Service
public class JwtService {

    private static final String SECRET_KEY =
            "FinanceOSSecretKeyFinanceOSSecretKeyFinanceOS2026";

    public String generateToken(String email) {

        return Jwts.builder()
                .subject(email)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24))
                .signWith(
                        SignatureAlgorithm.HS256,
                        SECRET_KEY.getBytes()
                )
                .compact();
    }
    public String extractEmail(String token) {

        Jws<Claims> claims = Jwts.parser()
                .setSigningKey(SECRET_KEY.getBytes())
                .build()
                .parseSignedClaims(token);

        return claims.getPayload().getSubject();
    }

    public boolean isTokenValid(String token) {

    try {

        Jwts.parser()
                .setSigningKey(SECRET_KEY.getBytes())
                .build()
                .parseSignedClaims(token);

        return true;

    } catch (Exception e) {

        e.printStackTrace();

        return false;
    }
}
}