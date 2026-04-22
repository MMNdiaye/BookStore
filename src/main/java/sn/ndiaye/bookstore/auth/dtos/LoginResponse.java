package sn.ndiaye.bookstore.auth.dtos;

import jakarta.servlet.http.Cookie;
import lombok.AllArgsConstructor;
import lombok.Data;
import sn.ndiaye.bookstore.auth.Jwt;

@AllArgsConstructor
@Data
public class LoginResponse {
    private Jwt accessToken;
    private Cookie refreshTokenCookie;
}
