package antoninopalazzolo.kitchensync.security;

import antoninopalazzolo.kitchensync.entity.Utente;
import antoninopalazzolo.kitchensync.exception.UnauthorizedException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.UUID;

// Responsabile della generazione e verifica del token JWT.
@Component
public class JWTTools {

    private final String secret;

    // Leggo il segreto da application.properties tramite @Value
    public JWTTools(@Value("${jwt.secret}") String secret) {
        this.secret = secret;
    }

    // Genero il token per l'utente loggato.
    // Dentro salvo l'id come "subject" — lo uso dopo nel JWTFilter
    // per recuperare l'utente dal database.
    public String generateToken(Utente utente) {
        return Jwts.builder()
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + 1000L * 60 * 60 * 24 * 7))
                .subject(String.valueOf(utente.getId()))
                .signWith(Keys.hmacShaKeyFor(secret.getBytes()))
                .compact();
    }

    // Verifico che il token sia valido e non scaduto.
    // Se non lo è lancio UnauthorizedException → HTTP 401.
    public void verifyToken(String token) {
        try {
            Jwts.parser()
                    .verifyWith(Keys.hmacShaKeyFor(secret.getBytes()))
                    .build()
                    .parseSignedClaims(token);
        } catch (Exception ex) {
            throw new UnauthorizedException("Token non valido o scaduto.");
        }
    }

    // Estraggo l'id dal token per recuperare l'utente nel JWTFilter.
    public UUID extractIdFromToken(String token) {
        return UUID.fromString(
                Jwts.parser()
                        .verifyWith(Keys.hmacShaKeyFor(secret.getBytes()))
                        .build()
                        .parseSignedClaims(token)
                        .getPayload()
                        .getSubject()
        );
    }
}
