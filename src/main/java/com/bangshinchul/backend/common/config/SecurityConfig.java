package com.bangshinchul.backend.common.config;

import com.bangshinchul.backend.auth.Auth;
import com.bangshinchul.backend.auth.AuthRepository;
import com.bangshinchul.backend.auth.AuthService;
import com.bangshinchul.backend.common.security.basic.CustomBasicAuthenticationEntryPoint;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.EnableGlobalAuthentication;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.parameters.P;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.authentication.AuthenticationWebFilter;
import org.springframework.security.web.server.authentication.ServerAuthenticationSuccessHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.servlet.http.HttpServletRequest;
import java.security.SecureRandom;

@Slf4j
@Configuration
@EnableWebSecurity
@ComponentScan(basePackages = {"com.bangshinchul.*"})
public class SecurityConfig extends WebSecurityConfigurerAdapter{

    private final AuthRepository authRepository;
    public SecurityConfig(AuthRepository authRepository) { this.authRepository = authRepository; }

    @Autowired
    UserDetailService userDetailService;

    @Autowired
    private CustomAuthenticationProvider authProvider;

    private static String REALM = "MY_TEST_REALM";

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        log.debug("]-----] SecurityConfig.configure::auth {} [-----[", auth);
        auth.authenticationProvider(authProvider);
        auth.userDetailsService(userDetailService).passwordEncoder(passwordEncoder());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                    // USER, ADMIN으로 권한 부여할 url 정의
                    .antMatchers(HttpMethod.OPTIONS,"/**").permitAll()
                    .antMatchers("/", "/auth/**").permitAll()
                    .anyRequest().authenticated()
//                    .antMatchers("/api/**").hasAnyRole("ADMIN","USER")
//                    .antMatchers("/admin/**").hasRole("ADMIN")
//                    .antMatchers("/api/**").authenticated()
//                    .antMatchers("/admin/**").authenticated()
                .and()
//                .httpBasic()
                .httpBasic().realmName(REALM).authenticationEntryPoint(getBasicAuthEntryPoint())
                .and()
                .logout()
                    // 로그아웃 관련 설정
                    .logoutRequestMatcher(new AntPathRequestMatcher("/auth/logout"))
                    .logoutSuccessUrl("/auth/logout-success")
                    .invalidateHttpSession(true)
                .and()
                    .csrf().disable()
                    // csrf 사용유무 설정
                    // csrf 설정을 사용하려면 모든 request에 csrf값을 함께 전달해야한다.
        //                .formLogin()
        // 로그인페이지 및 성공 url정의 및 로그인시 사용할 id, password 파라미터 정의
//                    .loginPage("/auth/login").failureUrl("/auth/login-error")
//                    .defaultSuccessUrl("/auth/login-success")
//                    .usernameParameter("id")
//                    .passwordParameter("password")
        ;
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    public CustomBasicAuthenticationEntryPoint getBasicAuthEntryPoint() {
        return new CustomBasicAuthenticationEntryPoint();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        SecureRandom random = new SecureRandom();
        byte bytes[] = new byte[16];
        random.nextBytes(bytes);
//        String hashFormat = String.format("$2y$%02d$", 10)+random;
//        return new BCryptPasswordEncoder(10, random);
        return new BCryptPasswordEncoder();
    }

}
