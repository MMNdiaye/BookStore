package sn.ndiaye.bookstore.loans.securityRules;

import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AuthorizeHttpRequestsConfigurer;
import org.springframework.stereotype.Component;
import sn.ndiaye.bookstore.commons.SecurityRules;
import sn.ndiaye.bookstore.users.entities.Role;

@Component
public class LoanSecurityRules implements SecurityRules {

    @Override
    public void configure(AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry registry) {
        registry.requestMatchers(HttpMethod.GET, "/loans/**").hasRole(Role.ADMIN.name())
                .requestMatchers(HttpMethod.PATCH, "/loans/**").hasRole(Role.ADMIN.name())
                .requestMatchers(HttpMethod.DELETE, "/loans/**").hasRole(Role.ADMIN.name());
    }
}
