## Basic Auth와 Jwt 토큰을 이용한 Spring Security 사용자인증 프로젝트입니다.
---

1. GET /auth/login URL에서 Basic Auth 사용자인증을 진행합니다.
2. Basic Auth 사용자인증이 성공하면 헤더를 통해 Jwt 토큰을 발급합니다.
3. Basic Auth 사용자인증이 실패하면 403 forbidden 에러를 리턴합니다.
4. GET /auth/login URL을 제외한 다른 모든 URL에 대해서 Jwt 토큰으로 사용자 인증을 진행합니다.
5. 만약 Jwt 토큰이 정확하지 않거나 요청 URL에 대해서 권한이 맞지 않으면 403 forbidden 에러를 리턴합니다.
