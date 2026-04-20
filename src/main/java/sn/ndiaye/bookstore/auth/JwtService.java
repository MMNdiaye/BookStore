package sn.ndiaye.bookstore.auth;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import sn.ndiaye.bookstore.users.User;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;

@AllArgsConstructor
@Service
public class JwtService {
    private JwtConfig jwtConfig;

    public String generateAccessToken(User user) {
        return generateToken(user, jwtConfig.getAccessTokenExpirationInSeconds());
    }

    public String generateRefreshToken(User user) {
        return generateToken(user, jwtConfig.getRefreshTokenExpirationInSeconds());
    }

    private String generateToken(User user, long expirationInSeconds) {
        return Jwts
                .builder()
                .subject(user.getId().toString())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + expirationInSeconds * 1000))
                .signWith(Keys.hmacShaKeyFor(jwtConfig.getSecretKey().getBytes()))
                .compact();
    }

    public boolean validate(String token) {
        try {
            var claims = getClaims(token);
            var expiration = claims.getExpiration();
            return expiration.after(new Date());
        } catch (JwtException exception) {
            return false;
        }
    }

    public Optional<UUID> getUserIdFromToken(String token) {
        try {
            var claims = getClaims(token);
            var userId = UUID.fromString(claims.getSubject());
            return Optional.of(userId);
        } catch (JwtException exception) {
            return Optional.empty();
        }
    }

    private Claims getClaims(String token) throws JwtException {
        return Jwts
                .parser()
                .verifyWith(Keys.hmacShaKeyFor(jwtConfig.getSecretKey().getBytes()))
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }


}
