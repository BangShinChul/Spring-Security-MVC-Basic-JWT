package com.bangshinchul.backend.common.config;

import com.bangshinchul.backend.BackendApplicationTests;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;

@Slf4j
@ActiveProfiles("local")
public class SecurityConfigTest  extends BackendApplicationTests {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    public void passwordEncoderTest() {
        String encryptString = passwordEncoder.encode("12345");
        log.info("]-----] encryptString [-----[ {}", encryptString);
    }
}