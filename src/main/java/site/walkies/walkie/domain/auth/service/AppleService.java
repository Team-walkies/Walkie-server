package site.walkies.walkie.domain.auth.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class AppleService {

    public String verifyIdTokenAndGetUserId(String idToken) {
        // 1. 애플의 Public Key로 JWT 검증 (애플 공식 문서 참고)
        // 2. 유효하면 payload에서 sub (고유 사용자 ID) 추출
        // 3. 검증 실패하면 예외 발생

        log.info("[AppleService] Received ID Token: {}", idToken);

        String appleUserId = "decoded-apple-user-id"; // 검증 후 user id 추출
        return appleUserId;
    }
}
