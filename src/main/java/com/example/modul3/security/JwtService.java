package com.example.modul3.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.Instant;

@Service
public class JwtService {

    private final JwtEncoder jwtEncoder;
    private final Duration expiration;

    public JwtService(
            @Value("${jwt.secret}") String secret,
            @Value("${jwt.access-token-expiration:15m}") Duration expiration,
            @Value("${jwt.algorithm:HS256}") String algorithmName) {

        MacAlgorithm algorithm = MacAlgorithm.from(algorithmName);
        if (algorithm == null || !"HS256".equals(algorithmName)) {
            throw new IllegalArgumentException(
                    "This JwtService currently supports only HS256");
        }

        byte[] keyBytes = secret.getBytes(StandardCharsets.UTF_8);
        if (keyBytes.length < 32) {
            throw new IllegalArgumentException(
                    "JWT_SECRET must be at least 32 bytes for HS256");
        }

        SecretKey key = new SecretKeySpec(keyBytes, "HmacSHA256");

        this.jwtEncoder = NimbusJwtEncoder.withSecretKey(key)
                .algorithm(algorithm)
                .build();
        this.expiration = expiration;
    }

    public String generateToken(String subject) {
        Instant now = Instant.now();

        JwtClaimsSet claims = JwtClaimsSet.builder()
                .subject(subject)
                .issuedAt(now)
                .expiresAt(now.plus(expiration))
                .build();

        return jwtEncoder
                .encode(JwtEncoderParameters.from(claims))
                .getTokenValue();
    }
}