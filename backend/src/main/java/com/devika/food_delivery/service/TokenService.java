package com.devika.food_delivery.service;

import com.devika.food_delivery.entity.User;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;

@Service
public class TokenService {
    private static final Duration LIFETIME = Duration.ofHours(1);
    private final JwtEncoder jwtEncoder;

    public TokenService(JwtEncoder jwtEncoder) {
        this.jwtEncoder = jwtEncoder;
    }

    public String issue(User user){
        Instant now = Instant.now();

        // 1. The claims: who this is, what they are, how long it's valid
        JwtClaimsSet claims = JwtClaimsSet.builder()
                .subject(user.getId().toString())
                .claim("role", user.getRole().name())
                .issuedAt(now)
                .expiresAt(now.plus(LIFETIME))
                .build();

        // 2. Sign with HS256 and the secret key
        JwsHeader header = JwsHeader.with(MacAlgorithm.HS256).build();
        JwtEncoderParameters params = JwtEncoderParameters.from(header, claims);

        return jwtEncoder.encode(params).getTokenValue();
    }

}
