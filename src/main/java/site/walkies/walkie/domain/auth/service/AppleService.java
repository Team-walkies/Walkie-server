package site.walkies.walkie.domain.auth.service;

import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import site.walkies.walkie.global.auth.utils.AppleJwtValidator;

@Slf4j
@Service
@RequiredArgsConstructor
public class AppleService {

    private final AppleJwtValidator appleJwtValidator;

    public String getAppleUserIdFromToken(String idToken) {
        log.info("[AppleService] Received ID Token: {}", idToken);

        // 1. id_token 유효성 검증 및 파싱
        Claims claims = appleJwtValidator.validateAndParseClaims(idToken);

        // 2. sub (Apple 사용자 고유 ID) 추출
        String appleUserId = claims.getSubject();

        log.info("[AppleService] Extracted Apple User ID (sub): {}", appleUserId);

        return appleUserId;
    }
}
