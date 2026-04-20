package sn.ndiaye.bookstore.auth;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import sn.ndiaye.bookstore.users.UserNotFoundException;
import sn.ndiaye.bookstore.users.UserRepository;

import java.io.IOException;
import java.util.Collections;

@AllArgsConstructor
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private JwtService jwtService;
    private UserRepository userRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        var authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        var token = authHeader.replace("Bearer ", "");
        if (!jwtService.validate(token)) {
            filterChain.doFilter(request, response);
            return;
        }

        var userId = jwtService.getUserIdFromToken(token)
                .orElseThrow(() -> new IllegalStateException("Validated token should be able to parse any claim"));

        var user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));


        var authentication = new UsernamePasswordAuthenticationToken(
                user.getId(),
                null,
                Collections.emptyList()
        );

        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        filterChain.doFilter(request, response);
    }
}
