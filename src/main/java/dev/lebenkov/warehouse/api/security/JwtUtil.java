package dev.lebenkov.warehouse.api.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.Date;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE)
public class JwtUtil {

    @Value("${jwt.secret}")
    String jwtSecret;

    public String generateToken(String username) {
        Date expirationDate = Date.from(ZonedDateTime.now().plusMinutes(60).toInstant());

        return JWT.create()
                .withSubject("User details")
                .withClaim("username", username)
                .withIssuedAt(new Date())
                .withIssuer("messenger-auth")
                .withExpiresAt(expirationDate)
                .sign(Algorithm.HMAC256(jwtSecret));
    }

    public String validateTokenAndRetrieveClaim(String token) {
        JWTVerifier jwtVerifier = JWT.require(Algorithm.HMAC256(jwtSecret))
                .withSubject("User details")
                .withIssuer("messenger-auth")
                .build();

        try {
            DecodedJWT jwt = jwtVerifier.verify(token);
            return jwt.getClaim("username").asString();
        } catch (JWTVerificationException exception) {
            throw new AccessDeniedException("Incorrect JWT Token");
        }
    }
}