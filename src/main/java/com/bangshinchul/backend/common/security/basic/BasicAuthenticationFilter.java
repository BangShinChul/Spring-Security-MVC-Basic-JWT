package com.bangshinchul.backend.common.security.basic;

import com.bangshinchul.backend.auth.Auth;
import com.bangshinchul.backend.auth.AuthRepository;
import com.bangshinchul.backend.common.security.jwt.JwtAuthenticationToken;
import com.bangshinchul.backend.common.security.jwt.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.RequestMatcher;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

import org.apache.commons.codec.binary.Base64;
import org.springframework.web.client.HttpClientErrorException;

/**
 * BasicAuthenticationFilter 클래스에서는 사용자인증 요청 시 Basic Auth 형태로
 * 요청이 들어오는지 확인 후
 * Basic Auth 형태로 요청이 들어오면 BasicAuthenticaitionProvider로 처리를 넘기고,
 * 아니면 에러를 리턴합니다.
 * */

@Slf4j
public class BasicAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

    public BasicAuthenticationFilter(RequestMatcher requestMatcher) {super(requestMatcher);}

    @Autowired
    AuthRepository authRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request,
                                                HttpServletResponse response) throws AuthenticationException, IOException {

        if (request.getHeader("Authorization") != null &&
                request.getHeader("Authorization").contains("Basic")) {

            String token = request.getHeader("Authorization").split("Basic ")[1];
            String[] basicAuthUsernameAndPassword = new String(new Base64().decode(token.getBytes())).split(":");

            String username = basicAuthUsernameAndPassword[0];
            String password = basicAuthUsernameAndPassword[1];

//            log.info(">>>>>>>> username AND password : {} : {}", username, password);

            Auth auth = authRepository.findByUsername(username);

            if(passwordEncoder.matches(password, auth.getPassword().split("}")[1])) {
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(username, password, auth.getAuthorities());
                return getAuthenticationManager().authenticate(authentication);
            }else {
                PrintWriter writer = response.getWriter();
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                writer.println("username and password is not correct. please check your account information!");

                throw new AccessDeniedException("username and password is not correct. please check your account information!");
            }
        } else {
            log.debug(">>>>>>>> BASIC TOKEN IS NULL");
            throw new AccessDeniedException("is Empty Basic Token");
        }
    }
}
