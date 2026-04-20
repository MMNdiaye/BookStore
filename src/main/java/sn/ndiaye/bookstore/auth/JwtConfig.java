package sn.ndiaye.bookstore.auth;

import lombok.Data;
import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Data
@Configuration
@ConfigurationProperties(prefix = "spring.jwt")
public class JwtConfig {
    private String secretKey;
    private Integer accessTokenExpirationInSeconds;
    private Integer refreshTokenExpirationInSeconds;
}
