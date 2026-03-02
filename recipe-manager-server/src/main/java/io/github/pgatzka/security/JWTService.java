package io.github.pgatzka.security;

import io.github.pgatzka.ApplicationProperties;
import io.github.pgatzka.data.pojo.AccountPojo;
import io.github.pgatzka.data.service.AccountDataService;
import io.github.pgatzka.exception.NotFoundException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.util.Date;
import java.util.UUID;
import java.util.function.Function;

import static io.github.pgatzka.jooq.Tables.ACCOUNT;

@Service
@RequiredArgsConstructor
public class JWTService {

    private static final String AUDIENCE = "api";

    private final ApplicationProperties properties;
    private final AccountDataService accountDataService;

    private SecretKey getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(properties.getJwt().getSecret());

        // Optional but recommended: fail fast with a clear message
        if (keyBytes.length < 32) { // 256 bits minimum for HS256
            throw new IllegalStateException("JWT secret is too short for HS256. Need at least 32 bytes after Base64-decoding.");
        }

        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String generateToken(String email) {
        AccountPojo account = accountDataService
                .fetchOptional(ACCOUNT.EMAIL.equalIgnoreCase(email))
                .orElseThrow(() -> new NotFoundException("Email is not registered"));

        Instant now = Instant.now();
        Instant exp = now.plusMillis(properties.getJwt().getExpiration());

        return Jwts.builder()
                .issuer(properties.getJwt().getIssuer())
                .subject(account.getId().toString())
                .audience().add(AUDIENCE).and()
                .issuedAt(Date.from(now))
                .expiration(Date.from(exp))
                .id(UUID.randomUUID().toString())
                .signWith(getSigningKey(), Jwts.SIG.HS256)
                .compact();
    }

    private Claims extractClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .requireIssuer(properties.getJwt().getIssuer())
                .requireAudience(AUDIENCE)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractClaims(token);
        return claimsResolver.apply(claims);
    }

    private Date extractExpiration(String token){
        return extractClaim(token, Claims::getExpiration);
    }

    public AccountPojo extractAccount(String token){
        String idString = extractClaim(token, Claims::getSubject);

        return accountDataService.fetchOptional(ACCOUNT.ID.eq(UUID.fromString(idString)))
                .orElseThrow(() -> new NotFoundException("Email is not registered"));
    }

    public boolean isValid(String token, UserDetails userDetails){
        AccountPojo account = extractAccount(token);
        return (account.getEmail().equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

}