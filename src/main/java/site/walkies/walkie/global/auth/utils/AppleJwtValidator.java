package site.walkies.walkie.global.auth.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.SecurityException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import site.walkies.walkie.global.auth.exception.InvalidJWTException;

import java.security.interfaces.RSAPublicKey;

@Slf4j
@Component
@RequiredArgsConstructor
public class AppleJwtValidator {

    public Claims validateAndParseClaims(String idToken) {
        try {
            // 1. JWT header에서 kid 추출
            String kid = AppleKeyUtils.extractKidFromToken(idToken);

            // 2. kid에 해당하는 Apple 공개키 가져오기
            RSAPublicKey publicKey = AppleKeyUtils.getApplePublicKeyByKid(kid);

            // 3. JWT 서명 검증 + claims 파싱
            JwtParser parser = Jwts.parserBuilder()
                    .setSigningKey(publicKey)
                    .build();

            Jws<Claims> jws = parser.parseClaimsJws(idToken);
            return jws.getBody();
        } catch (SecurityException e) {
            throw new InvalidJWTException("Apple id_token의 서명 검증에 실패했습니다.", e);
        } catch (Exception e) {
            throw new InvalidJWTException("Apple id_token 파싱 중 오류가 발생했습니다.", e);
        }
    }
}
