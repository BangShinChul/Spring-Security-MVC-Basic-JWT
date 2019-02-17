package com.bangshinchul.backend.common.config;

import com.bangshinchul.backend.BackendApplicationTests;
import com.bangshinchul.backend.auth.Auth;
import com.bangshinchul.backend.auth.AuthRepository;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.util.AssertionErrors.assertTrue;

@ActiveProfiles("local")
public class SecurityConfigTest  extends BackendApplicationTests {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthRepository authRepository;

    @Before
    public void setUp() throws Exception {
        passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Test
    public void passwordEncoderTest() {
        String encryptString = passwordEncoder.encode("12345");
        // $2a$10$gFRn2QA0RuAs2LelQwW19e0s8uJTz6j64bDXiDCNxG2pT.5RTBvY2
        // $2a$10$xfKp9yhIHOpvDVk95S3OUeTVZo35aG63/ratGYOsLtCqhSsRyI3QC
        log.info("]-----] encryptString [-----[ {}", encryptString);
    }

    @Test
    public void bcryptPasswordMatchTest() {

        /*

        * 테스트코드에서는 정상적으로 잘돌아가나,
        * 운영에서의 동작은 bcrypt로 해싱된 패스워드에서
        * {bcrypt}를 제거해 준 상태의 String 값으로 비교를 해야 정상적으로 돌아간다.
        *
        * 이 이슈에 대해서 정확한 원인은 잘 모르겠으나,
        * 해싱된 패스워드를 테스트코드로 insert 해서 발생한 이슈일 수도 있을것으로 짐작되므로
        * 추후에 운영환경에서 db에 해싱된 패스워드를 insert해보고
        * 다시 한번 운영환경에서 테스트해볼 것.
        * */
        Auth auth = authRepository.findByUsername("test123");
        log.info("=========== SELECT RESULT : {}", auth);
        log.info("=========== password : 12345");
        log.info("=========== bcrypt password : {}", auth.getPassword());

        if(passwordEncoder.matches("12345", auth.getPassword())) {
            log.info("]-----]password is match![-----[");
        } else {
            log.info("]-----]password is NOT match![-----[");
        }
    }

    @Test
    public void Test() {
        String pwd = passwordEncoder.encode("12345");

        log.info("]-----]bcrypt pwd[-----[ : {}" , pwd);
        if(passwordEncoder.matches("12345", pwd)) {
            log.info("]-----]password is match![-----[");
        } else {
            log.info("]-----]password is NOT match![-----[");
        }
    }

    @Test
    public void encode() {
        String password = "password";

        String encPassword = passwordEncoder.encode(password);

        assertThat(passwordEncoder.matches(password, encPassword)).isTrue();
        assertThat(encPassword).contains("{bcrypt}");
    }

}