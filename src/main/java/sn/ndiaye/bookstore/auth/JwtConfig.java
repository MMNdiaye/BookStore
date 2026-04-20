package sn.ndiaye.bookstore.auth;

import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import javax.crypto.SecretKey;

@Data
@Configuration
@ConfigurationProperties(prefix = "spring.jwt")
public class JwtConfig {
    private String secret;
    private Integer accessTokenExpirationInSeconds;
    private Integer refreshTokenExpirationInSeconds;
    private SecretKey secretKey;

    @PostConstruct
    private void initKey() {
        secretKey = Keys.hmacShaKeyFor(secret.getBytes());
    }
}
