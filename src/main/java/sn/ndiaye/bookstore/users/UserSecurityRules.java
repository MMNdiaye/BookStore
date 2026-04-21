package sn.ndiaye.bookstore.users;

import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AuthorizeHttpRequestsConfigurer;
import org.springframework.stereotype.Component;
import sn.ndiaye.bookstore.commons.SecurityRules;

@Component
public class UserSecurityRules implements SecurityRules {
    @Override
    public void configure(AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry registry) {
        registry.requestMatchers(HttpMethod.POST,"/users").permitAll()
                .requestMatchers(HttpMethod.GET, "/users/**").hasRole(Role.ADMIN.name());
    }
}
