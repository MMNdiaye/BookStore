package sn.ndiaye.bookstore.auth;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import sn.ndiaye.bookstore.users.User;

import java.util.Date;

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
}
