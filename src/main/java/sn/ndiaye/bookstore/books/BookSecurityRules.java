package sn.ndiaye.bookstore.books;

import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AuthorizeHttpRequestsConfigurer;
import sn.ndiaye.bookstore.commons.SecurityRules;
import sn.ndiaye.bookstore.users.Role;

public class BookSecurityRules implements SecurityRules {
    @Override
    public void configure(AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry registry) {
        registry.requestMatchers(HttpMethod.POST, "/users/**").hasRole(Role.ADMIN.name());
    }
}
