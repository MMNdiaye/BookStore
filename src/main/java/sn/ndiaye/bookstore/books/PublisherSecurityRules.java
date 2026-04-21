package sn.ndiaye.bookstore.books;

import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AuthorizeHttpRequestsConfigurer;
import sn.ndiaye.bookstore.commons.SecurityRules;
import sn.ndiaye.bookstore.users.Role;

public class PublisherSecurityRules implements SecurityRules {
    @Override
    public void configure(AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry registry) {
        registry.requestMatchers(HttpMethod.POST, "/publishers").hasRole(Role.ADMIN.name());
    }
}
