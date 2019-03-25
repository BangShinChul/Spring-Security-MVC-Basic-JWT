package com.bangshinchul.backend.auth;

import com.bangshinchul.backend.BackendApplicationTests;
import com.bangshinchul.backend.common.config.SecurityConfig;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("local")
public class AuthRepositoryTest extends BackendApplicationTests {

    @Autowired
    private AuthRepository authRepository;

    @Autowired
    private AuthRoleRepository authRoleRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Before
    public void setUp() throws Exception {
        passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Test
    public void setRole() {
        Role roleAdmin = new Role();
        roleAdmin.setRole("ADMIN");
        roleRepository.save(roleAdmin);

        Role roleUser = new Role();
        roleUser.setRole("USER");
        roleRepository.save(roleUser);
    }

    @Test
    public void saveTest() {
        Auth auth = new Auth();
        auth.setUsername("admin");
        auth.setPassword(passwordEncoder.encode("12345"));
        authRepository.save(auth);

        AuthRole authRoleAdmin = new AuthRole();
        authRoleAdmin.setRoleId(Long.valueOf(1));
        authRoleAdmin.setAuthId(auth.getId());
        authRoleRepository.save(authRoleAdmin);

        AuthRole authRoleUser = new AuthRole();
        authRoleUser.setRoleId(Long.valueOf(2));
        authRoleUser.setAuthId(auth.getId());
        authRoleRepository.save(authRoleUser);
    }

    @Test
    public void saveAdminTest() {
        Auth auth = new Auth();
        auth.setUsername("admin");
        auth.setPassword(passwordEncoder.encode("12345"));
        authRepository.save(auth);

        Role roleAdmin = roleRepository.findByRole("ADMIN");

        AuthRole authRoleUser = new AuthRole();
        authRoleUser.setRoleId(roleAdmin.getId());
        authRoleUser.setAuthId(auth.getId());
        authRoleRepository.save(authRoleUser);
    }

    @Test
    public void saveUserTest() {
        Auth auth = new Auth();
        auth.setUsername("test123");
        auth.setPassword(passwordEncoder.encode("12345"));
        authRepository.save(auth);

        Role roleUser = roleRepository.findByRole("USER");

        AuthRole authRoleUser = new AuthRole();
        authRoleUser.setRoleId(roleUser.getId());
        authRoleUser.setAuthId(auth.getId());
        authRoleRepository.save(authRoleUser);
    }

    @Test
    public void findByIdTest() {
        Auth auth = authRepository.findByUsername("admin");
        log.debug("]-----] auth [-----[ {}", auth);

        Auth auth2 = authRepository.findById(1l).get();
        log.debug("]-----] auth2 [-----[ {}", auth2);
    }

}