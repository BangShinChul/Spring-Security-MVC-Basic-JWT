package com.bangshinchul.backend.auth;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class AuthService {

    private  final AuthRepository authRepository;

    public AuthService(AuthRepository authRepository) { this.authRepository = authRepository; }

    public Auth findByUsername(String username) {
        return authRepository.findByUsername(username);
    }
}
