package sn.ndiaye.bookstore.auth;

import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.web.bind.annotation.*;
import sn.ndiaye.bookstore.users.UserDto;
import sn.ndiaye.bookstore.users.UserMapper;
import sn.ndiaye.bookstore.users.UserNotFoundException;

@AllArgsConstructor
@RestController
@RequestMapping("/auth")
public class AuthController {
    private AuthService authService;
    private UserMapper userMapper;

    @PostMapping("/login")
    public ResponseEntity<JwtResponse> loginUser(
            @RequestBody LoginRequest request,
            HttpServletResponse response
    ) {
        var loginResponse = authService.login(request);
        var jwtResponse = new JwtResponse(loginResponse.getAccessToken());
        response.addCookie(loginResponse.getRefreshTokenCookie());
        return ResponseEntity.ok(jwtResponse);
    }

    @GetMapping("/me")
    public ResponseEntity<UserDto> getProfile() {
        var user = authService.getCurrentUser();
        return ResponseEntity.ok(userMapper.toDto(user));
    }

    @PostMapping("/refresh")
    public ResponseEntity<JwtResponse> refreshToken(
            @CookieValue String refreshToken
    ) {
        var jwtResponse = authService.refreshAccess(refreshToken);
        return ResponseEntity.ok(jwtResponse);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<Void> handleBadCredentials() {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    @ExceptionHandler({CredentialsExpiredException.class, UserNotFoundException.class})
    private ResponseEntity<Void> handleRefreshFail() {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
}
