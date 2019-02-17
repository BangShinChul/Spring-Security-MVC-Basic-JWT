package com.bangshinchul.backend.common.security.jwt;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.util.StringUtils;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
public class JwtAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

    public JwtAuthenticationFilter(RequestMatcher requestMatcher) {
        super(requestMatcher);
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request,
                                                HttpServletResponse response) throws AuthenticationException {
        log.info(">>>>>>> JWT WWW-Authenticate check : {}", request.getHeader("Authorization"));
        log.info(">>>>>>> JWT WWW-Authenticate check2 : {}",  request.getHeader("Authorization").contains("Bearer"));

        if (!request.getHeader("Authorization").isEmpty() &&
                request.getHeader("Authorization").contains("Bearer")
        ) {
            String token = request.getHeader("Authorization").split("Bearer ")[1];

            if (token.isEmpty()) { // null이면 true리턴, 아니면 false 리턴
                throw new AccessDeniedException("is Empty Bearer Token");
            } else {
                return getAuthenticationManager().authenticate(new JwtAuthenticationToken(token));
            }
        }else {
            throw new AccessDeniedException("is Empty Bearer Authorization Header");
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request,
                                            HttpServletResponse response,
                                            FilterChain chain,
                                            Authentication authResult) throws IOException, ServletException {
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(authResult);
        SecurityContextHolder.setContext(context);
        chain.doFilter(request, response);
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request,
                                              HttpServletResponse response,
                                              AuthenticationException failed) throws IOException, ServletException {
        SecurityContextHolder.clearContext();
        getFailureHandler().onAuthenticationFailure(request, response, failed);
    }
}
