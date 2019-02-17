package com.bangshinchul.backend.common.security.basic;

import com.bangshinchul.backend.auth.Auth;
import com.bangshinchul.backend.auth.AuthRepository;
import com.bangshinchul.backend.common.security.jwt.JwtAuthenticationToken;
import com.bangshinchul.backend.common.security.jwt.JwtUserDetailsService;
import com.bangshinchul.backend.common.security.jwt.JwtUtil;
import com.bangshinchul.backend.common.utils.HashUtil;
import lombok.extern.slf4j.Slf4j;
import org.bouncycastle.crypto.generators.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.parameters.P;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 해당 클래스에서는 BasicAuthenticationFilter에서 넘겨준
 * Basic Auth 형태로 사용자인증 요청이 들어왔을 때,
 * 해당 클래스의 authenticate 메서드에서 사용자 정보를 확인하고
 * 인증 처리 혹은 실패를 결정하여 결과를 리턴합니다.
 *
 * 해당 클래스에서 인증에 실패할 경우
 * CustomBasicAuthenticationEntryPoint 클래스로 작업을 넘겨 나머지 과정을 처리하게 됩니다.
 */


@Slf4j
@Component
public class BasicAuthenticationProvider implements AuthenticationProvider {

    @Autowired
    AuthRepository authRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = authentication.getName();
        String password = authentication.getCredentials().toString();
        Auth auth = authRepository.findByUsername(username);

        if (passwordEncoder.matches(password, auth.getPassword().split("}")[1])) {
            log.info("]-----]password is match![-----[");
            // Security에 사용자인증 요청한 계정 Authentication 등록
            SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(username, password, auth.getAuthorities()));
            return new UsernamePasswordAuthenticationToken(username, password, auth.getAuthorities());
        }else {
            log.info("]-----]password is NOT match![-----[");
            throw new AccessDeniedException("username and password is not correct. please check your account infomation!");
        }
    }

    @Override
    public boolean supports(final Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}
