package sn.ndiaye.bookstore.auth;

import jakarta.servlet.http.Cookie;
import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class LoginResponse {
    private Jwt accessToken;
    private Cookie refreshTokenCookie;
}
