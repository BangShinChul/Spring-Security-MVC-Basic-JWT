package com.bangshinchul.backend.auth.mapper;

import com.bangshinchul.backend.BackendApplicationTests;
import com.bangshinchul.backend.auth.Auth;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.Assert.*;


@ActiveProfiles("local")
public class AuthMapperProviderTest extends BackendApplicationTests {

    @Autowired
    private AuthMapper authMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Before
    public void setUp() throws Exception {
        passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Test
    public void findByUsernameAndPassword() {
        String username = "test";
        String password = passwordEncoder.encode("12345");
        Auth auth = authMapper.findByUsernameAndPassword(username, "12345");
        log.debug("]-----] findByIdAndPasswordTest [-----[ {}", auth);
    }
}