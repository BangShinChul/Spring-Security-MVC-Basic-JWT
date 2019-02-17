package com.bangshinchul.backend.common.security.jwt;
import com.auth0.jwt.algorithms.Algorithm;
import java.io.UnsupportedEncodingException;

public class JwtInfo {

    public static final String HEADER_NAME = "Authorization";

    public static final String ISSUER = "bsc";

    public static final String TOKEN_KEY = "bangshinchulapitokenkey";

    public static final long EXPIRES_LIMIT = 3L;

    public static Algorithm getAlgorithm() {
        try {
            return Algorithm.HMAC256(JwtInfo.TOKEN_KEY);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            return Algorithm.none();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return Algorithm.none();
        }
    }
}
