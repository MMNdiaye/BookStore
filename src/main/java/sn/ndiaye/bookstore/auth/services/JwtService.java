package sn.ndiaye.bookstore.auth.services;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;
import sn.ndiaye.bookstore.auth.Jwt;
import sn.ndiaye.bookstore.auth.config.JwtConfig;
import sn.ndiaye.bookstore.users.entities.User;
import java.util.Date;

@AllArgsConstructor
@Service
public class JwtService {
    private final JwtConfig jwtConfig;

    public Jwt generateAccessToken(User user) {
        return generateToken(user, jwtConfig.getAccessTokenExpirationInSeconds());
    }

    public Jwt generateRefreshToken(User user) {
        return generateToken(user, jwtConfig.getRefreshTokenExpirationInSeconds());
    }

    private Jwt generateToken(User user, long expirationInSeconds) {
        var claims = Jwts.claims()
                .subject(user.getId().toString())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + expirationInSeconds * 1000))
                .add("role", user.getRole())
                .build();
        return new Jwt(claims, jwtConfig.getSecretKey());
    }

    public Jwt parseToken(String token) {
        try {
            var secretKey = jwtConfig.getSecretKey();
            var claims = Jwts.parser()
                    .verifyWith(secretKey)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
            return new Jwt(claims, secretKey);
        } catch (JwtException exception) {
            throw new BadCredentialsException("Invalid refresh token");
        }
    }
}
