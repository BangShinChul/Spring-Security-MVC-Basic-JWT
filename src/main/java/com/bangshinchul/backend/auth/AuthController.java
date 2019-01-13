package com.bangshinchul.backend.auth;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

@Slf4j
@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    AuthRepository authRepository;

    @GetMapping("/login")
    public String signIn() {
        log.debug("]-----] AuthController.login::call::GET[-----[");
        return "Please login";
    }

    @PostMapping("/login")
    public String login(@RequestBody Auth request) {
        log.debug("]-----] AuthController.login::call [-----[");

        String username = request.getUsername();
        String password = request.getPassword();

        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(username, password);
        Authentication authentication = authenticationManager.authenticate(token);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        return "Login Success!";
    }

    @PostMapping("/login-basic")
    public String loginBasic(@AuthenticationPrincipal Auth request) {
        return "welcome user " + request.getUsername();
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    @GetMapping("db-test")
    public Auth test() {
        return authRepository.findByUsername("user");
    }

    @GetMapping("/login-success")
    public String signInSuccess() {
        return "Hello world!";
    }

    @GetMapping("/login-error")
    public String signInError() {
        return "Login error occur!!";
    }
}
