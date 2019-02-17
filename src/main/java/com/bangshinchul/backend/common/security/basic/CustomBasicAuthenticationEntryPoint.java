package com.bangshinchul.backend.common.security.basic;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.www.BasicAuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@Slf4j
@Component
public class CustomBasicAuthenticationEntryPoint extends BasicAuthenticationEntryPoint {

    @Override
    public void commence(
            final HttpServletRequest request,
            final HttpServletResponse response,
            final AuthenticationException authException) throws IOException, ServletException {

        PrintWriter writer = response.getWriter();

        log.debug("\n]-----]CustomBasicAuthenticationEntryPoint CALL[-----[\n");
        log.debug("\n]-----]CustomBasicAuthenticationEntryPoint CHECK REQUEST[-----[ : {}\n", request); // BasicAuth check

        String token = request.getHeader("Authorization");
        writer.println("token : "+token);

        if (!token.isEmpty()) {
            log.info("]-----]CustomBasicAuthenticationEntryPoint::TOKEN[-----[ : {}", token);
        }

        // Authentication failed, send error response.
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // 401 return
        response.addHeader("WWW-Authenticate", "Basic realm=" + getRealmName() + "");

//        writer.println("HTTP Status 401 : " + authException.getMessage());
        writer.println("HTTP Status 401 : username and password is not correct. please check your account infomation! : \n"+request.getHeader("Authorization"));
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        log.debug("]-----]afterPropertiesSet CALL[-----[");
        setRealmName("MY_TEST_REALM");
        super.afterPropertiesSet();
    }
}
