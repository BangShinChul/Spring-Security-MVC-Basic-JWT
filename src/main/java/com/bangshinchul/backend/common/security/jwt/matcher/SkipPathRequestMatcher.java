package com.bangshinchul.backend.common.security.jwt.matcher;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.OrRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
public class SkipPathRequestMatcher implements RequestMatcher {

    private OrRequestMatcher skipRequestMatcher;

    public SkipPathRequestMatcher(List<String> skipPathList) {
        log.info("]-----]SkipPathRequestMatcher CALL : {}[-----[", skipPathList.stream().toString());
        if(!skipPathList.isEmpty()) {
            List<RequestMatcher> requestMatcherList = skipPathList.stream()
                    .map(AntPathRequestMatcher::new)
                    .collect(Collectors.toList());
            skipRequestMatcher = new OrRequestMatcher(requestMatcherList);

            log.info(">>>>>>> skipPathList : {}", skipRequestMatcher.toString());
        }
    }

    @Override
    public boolean matches(HttpServletRequest request) {
        return !skipRequestMatcher.matches(request);
    }
}
