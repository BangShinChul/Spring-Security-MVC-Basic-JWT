package com.bangshinchul.backend.common.config;

import com.bangshinchul.backend.auth.AuthRepository;
import com.bangshinchul.backend.common.security.basic.BasicAuthenticationFilter;
import com.bangshinchul.backend.common.security.basic.BasicAuthenticationProvider;
import com.bangshinchul.backend.common.security.basic.CustomBasicAuthenticationEntryPoint;
import com.bangshinchul.backend.common.security.jwt.BaseSecurityHandler;
import com.bangshinchul.backend.common.security.jwt.JwtAuthenticationFilter;
import com.bangshinchul.backend.common.security.jwt.JwtAuthenticationProvider;
import com.bangshinchul.backend.common.security.jwt.matcher.SkipPathRequestMatcher;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import java.security.SecureRandom;
import java.util.Arrays;

@Slf4j
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true) // 컨트롤러에서 preAuthorize 어노테이션 사용하기위해 필요
//@ComponentScan(basePackages = {"com.bangshinchul.**"})
public class SecurityConfig extends WebSecurityConfigurerAdapter{

    private final AuthRepository authRepository;
    public SecurityConfig(AuthRepository authRepository) { this.authRepository = authRepository; }

    @Autowired
    UserDetailService userDetailService;

    @Autowired
    private BasicAuthenticationProvider authProvider;

    @Autowired
    private JwtAuthenticationProvider jwtProvider;

    @Autowired
    private BaseSecurityHandler securityHandler;

    private static final String LOGIN_ENTRY_POINT = "/auth/**";
    private static final String ERROR_ENTRY_POINT = "/error/**";
    private static final String ROOT_ENTRY_POINT = "/**";
    private static String REALM = "MY_TEST_REALM";

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        log.debug("]-----] SecurityConfig.configure::auth {} [-----[", auth);
        auth.authenticationProvider(authProvider).authenticationProvider(jwtProvider);
//        auth.authenticationProvider(jwtProvider).authenticationProvider()
        auth.userDetailsService(userDetailService).passwordEncoder(passwordEncoder());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .addFilterBefore(basicAuthenticationFilter(), FilterSecurityInterceptor.class)
                .addFilterBefore(jwtAuthenticationFilter(), FilterSecurityInterceptor.class)
                .csrf().disable()
                .authorizeRequests()
                    // USER, ADMIN으로 권한 부여할 url 정의
                    .antMatchers(HttpMethod.OPTIONS,ROOT_ENTRY_POINT).permitAll()
                    .antMatchers(ROOT_ENTRY_POINT, LOGIN_ENTRY_POINT).permitAll()
                  .anyRequest().authenticated()
//                    .antMatchers("/api/**").hasAnyRole("ADMIN","USER")
//                    .antMatchers("/admin/**").hasRole("ADMIN")
//                    .antMatchers("/api/**").authenticated()
//                    .antMatchers("/admin/**").authenticated()
//                .and()
//                .httpBasic().realmName(REALM).authenticationEntryPoint(getBasicAuthEntryPoint())
//                .logout()
//                    // 로그아웃 관련 설정
//                    .logoutRequestMatcher(new AntPathRequestMatcher("/auth/logout"))
//                    .logoutSuccessUrl("/auth/logout-success")
//                    .invalidateHttpSession(true)
//                .and()
//                    .csrf().disable()
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

//
    @Bean
    public AntPathRequestMatcher basicAntPathRequestMatcher() {
        return new AntPathRequestMatcher("/auth/**");
    }
//
    @Bean
    public BasicAuthenticationFilter basicAuthenticationFilter() throws Exception {
        log.debug("]-----]SecurityConfig.basicAuthenticationFilter call[-----[");
        BasicAuthenticationFilter filter = new BasicAuthenticationFilter(basicAntPathRequestMatcher());
        filter.setAuthenticationManager(authenticationManager());
        filter.setAuthenticationSuccessHandler(securityHandler);
        filter.setAuthenticationFailureHandler(securityHandler);
        return filter;
    }
//

    @Bean
    public SkipPathRequestMatcher skipPathRequestMatcher() {
        // basic auth를 제외한 jwt 검사 필터를 위한 스킵 리퀘스트 매쳐
        return new SkipPathRequestMatcher(Arrays.asList(LOGIN_ENTRY_POINT, ERROR_ENTRY_POINT));
    }

    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter() throws Exception {
        log.debug("]-----]SecurityConfig.jwtAuthenticationFilter call[-----[");
        JwtAuthenticationFilter filter = new JwtAuthenticationFilter(skipPathRequestMatcher());
        filter.setAuthenticationManager(authenticationManager());
        filter.setAuthenticationFailureHandler(securityHandler);
        return filter;
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        log.info("]-----]authenticationManagerBean call[----[");
        return super.authenticationManagerBean();
    }

    @Bean
    public CustomBasicAuthenticationEntryPoint getBasicAuthEntryPoint() {
        log.info("]-----]getBasicAuthEntryPoint call[----[");
        return new CustomBasicAuthenticationEntryPoint();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        SecureRandom random = new SecureRandom();
        byte bytes[] = new byte[16];
        random.nextBytes(bytes);
        return new BCryptPasswordEncoder();
    }

}
