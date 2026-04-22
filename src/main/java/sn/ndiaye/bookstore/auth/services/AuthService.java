package sn.ndiaye.bookstore.auth.services;

import jakarta.servlet.http.Cookie;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import sn.ndiaye.bookstore.auth.config.JwtConfig;
import sn.ndiaye.bookstore.auth.dtos.JwtResponse;
import sn.ndiaye.bookstore.auth.dtos.LoginRequest;
import sn.ndiaye.bookstore.auth.dtos.LoginResponse;
import sn.ndiaye.bookstore.users.entities.User;
import sn.ndiaye.bookstore.users.exceptions.UserNotFoundException;
import sn.ndiaye.bookstore.users.repositories.UserRepository;
import java.util.UUID;

@AllArgsConstructor
@Service
public class AuthService {
    private AuthenticationManager authenticationManager;
    private JwtService jwtService;
    private UserRepository userRepository;
    private JwtConfig jwtConfig;

    public LoginResponse login(LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );
        var user = userRepository.findByEmail(request.getEmail()).orElseThrow();
        var accessToken = jwtService.generateAccessToken(user);
        var refreshToken = jwtService.generateRefreshToken(user);
        var cookie = new Cookie("refreshToken", refreshToken.toString());
        cookie.setHttpOnly(true);
        cookie.setPath("/auth/refresh");
        cookie.setSecure(true);
        cookie.setMaxAge(jwtConfig.getRefreshTokenExpirationInSeconds());
        return new LoginResponse(accessToken, cookie);
    }

    public User getCurrentUser() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        var id = (UUID) authentication.getPrincipal();
        return userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));
    }

    public JwtResponse refreshAccess(String refreshToken) {
        var refreshJwt = jwtService.parseToken(refreshToken);
        if (refreshJwt.isExpired())
            throw new BadCredentialsException("Your session is expired.");

        var user = userRepository.findById(refreshJwt.getUserId())
                .orElseThrow(() -> new UserNotFoundException(refreshJwt.getUserId()));

        var accessToken = jwtService.generateAccessToken(user);
        return new JwtResponse(accessToken.toString());
    }
}
