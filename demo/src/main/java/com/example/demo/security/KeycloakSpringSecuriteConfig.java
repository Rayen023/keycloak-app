package com.example.demo.security;

import org.keycloak.adapters.springsecurity.KeycloakConfiguration;
import org.keycloak.adapters.springsecurity.config.KeycloakWebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.web.authentication.session.RegisterSessionAuthenticationStrategy;
import org.springframework.security.web.authentication.session.SessionAuthenticationStrategy;

@KeycloakConfiguration
public class KeycloakSpringSecuriteConfig extends KeycloakWebSecurityConfigurerAdapter {

    @Override
    protected SessionAuthenticationStrategy sessionAuthenticationStrategy() {
        return new RegisterSessionAuthenticationStrategy(new SessionRegistryImpl());
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(keycloakAuthenticationProvider());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        super.configure(http);
        http.authorizeRequests().antMatchers("/api/demo/**").hasAuthority("ROLE_ADMIN");
        http.authorizeRequests().antMatchers("/api/manager/**").hasAuthority("ROLE_MANAGER");
        http.authorizeRequests().antMatchers("/**").permitAll();
        // http.authorizeRequests().antMatchers("/**").authenticated();
        // expressionInterceptUrlRegistry =
        // expressionInterceptUrlRegistry.antMatchers("/api/FilliereB*").hasRole("FILLIEREB");
    }

}