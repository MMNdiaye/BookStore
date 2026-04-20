package sn.ndiaye.bookstore.auth;

import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import sn.ndiaye.bookstore.users.User;
import sn.ndiaye.bookstore.users.UserRepository;

import java.util.Collections;

@AllArgsConstructor
@Service
public class AuthService {
    private AuthenticationManager authenticationManager;
    private JwtService jwtService;
    private UserRepository userRepository;

    public JwtResponse login(LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );
        var user = userRepository.findByEmail(request.getEmail()).orElseThrow();
        var accessToken = jwtService.generateToken(user);
        return new JwtResponse(accessToken);
    }

}
