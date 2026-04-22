package sn.ndiaye.bookstore.books.securityRules;

import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AuthorizeHttpRequestsConfigurer;
import org.springframework.stereotype.Component;
import sn.ndiaye.bookstore.commons.SecurityRules;
import sn.ndiaye.bookstore.users.entities.Role;

@Component
public class GenreSecurityRules implements SecurityRules {

    @Override
    public void configure(AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry registry) {
        registry.requestMatchers(HttpMethod.POST, "/genres/**").hasRole(Role.ADMIN.name())
                .requestMatchers(HttpMethod.GET, "/genres/*").permitAll();
    }
}
