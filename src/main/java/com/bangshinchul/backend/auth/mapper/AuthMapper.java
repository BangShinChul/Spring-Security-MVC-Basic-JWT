package com.bangshinchul.backend.auth.mapper;

import com.bangshinchul.backend.auth.Auth;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.SelectProvider;

@Mapper
public interface AuthMapper {
    @SelectProvider(type = AuthMapperProvider.class, method = "findByUsernameAndPassword")
    Auth findByUsernameAndPassword(@Param("username") String username, @Param("password") String password);

    @SelectProvider(type = AuthMapperProvider.class, method = "findUsernameByUsername")
    String findUsernameByUsername(@Param("username") String username);
}
