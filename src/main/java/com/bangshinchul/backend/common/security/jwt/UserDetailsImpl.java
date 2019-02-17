package com.bangshinchul.backend.common.security.jwt;

import com.bangshinchul.backend.auth.Auth;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.List;

public class UserDetailsImpl extends User {

    public UserDetailsImpl(String id, List<GrantedAuthority> authorities) {
        super(id, "", authorities);
    }

    public UserDetailsImpl(Auth auth, List<GrantedAuthority> authorities) {
        super(auth.getUsername(), auth.getPassword(), authorities);
    }
}
