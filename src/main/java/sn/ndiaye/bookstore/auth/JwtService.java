package sn.ndiaye.bookstore.auth;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import sn.ndiaye.bookstore.users.User;

import javax.swing.text.html.Option;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;

@Service
public class JwtService {
    @Value("${spring.jwt.secretKey}")
    private String secretKey;

    public String generateToken(User user) {
        return Jwts
                .builder()
                .subject(user.getId().toString())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + 600 * 1000))
                .signWith(Keys.hmacShaKeyFor(secretKey.getBytes()))
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
                .verifyWith(Keys.hmacShaKeyFor(secretKey.getBytes()))
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }


}
