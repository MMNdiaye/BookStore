package sn.ndiaye.bookstore.books.securityRules;

import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AuthorizeHttpRequestsConfigurer;
import sn.ndiaye.bookstore.commons.SecurityRules;
import sn.ndiaye.bookstore.users.entities.Role;

public class BookSecurityRules implements SecurityRules {
    @Override
    public void configure(AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry registry) {
        registry.requestMatchers(HttpMethod.POST, "/books").hasRole(Role.ADMIN.name())
                .requestMatchers(HttpMethod.GET, "/books/**").permitAll()
                .requestMatchers(HttpMethod.PATCH, "/books/**").hasRole(Role.ADMIN.name());
    }
}
