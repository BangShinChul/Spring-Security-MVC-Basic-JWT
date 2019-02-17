package com.bangshinchul.backend.sample;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.access.prepost.PreFilter;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api")
public class SampleController {

//    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
//    @PreAuthorize("hasAnyAuthority('ADMIN','USER')")
    @GetMapping("/test-all")
    public String testAll() {
        return "Login Success : ADMIN & USER";
    }

//    @PreAuthorize("hasRole('ADMIN')")
//    @PreAuthorize("hasAuthority('ADMIN')")
    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/test-admin")
    public String testAdmin() {
        return "Login Success : ADMIN";
    }

//    @PreAuthorize("hasRole('USER')")
//    @PreAuthorize("hasAuthority('USER')")
    @PostAuthorize("hasAuthority('USER')")
    @GetMapping("/test-user")
    public String testUser() {
        return "Login Success : USER";
    }

}
