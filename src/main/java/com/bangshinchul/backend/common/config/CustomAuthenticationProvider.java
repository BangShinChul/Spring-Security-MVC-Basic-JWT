package com.bangshinchul.backend.common.config;

import com.bangshinchul.backend.auth.Auth;
import com.bangshinchul.backend.auth.AuthRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
public class CustomAuthenticationProvider implements AuthenticationProvider {

    @Autowired
    AuthRepository authRepository;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = authentication.getName();
        String password = authentication.getCredentials().toString();

        log.debug("]-----]CustomAuthenticationProvider.authenticate::authentication {} [-----[", authentication);
        Auth auth = authRepository.findByUsername(username);
        log.debug("]-----]CustomAuthenticationProvider.authenticate::auth {} [-----[", auth);

        if (password.equals("12345")) {
            SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(username, password, auth.getAuthorities()));
            return new UsernamePasswordAuthenticationToken(username, password, auth.getAuthorities());
        } else {
            return null;
        }
    }

    @Override
    public boolean supports(final Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}
