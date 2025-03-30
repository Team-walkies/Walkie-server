package site.walkies.walkie.global.auth.utils;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import site.walkies.walkie.global.auth.exception.InvalidJWTException;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.time.Clock;
import java.util.Base64;
import java.util.Date;

@Component
@RequiredArgsConstructor
@Slf4j
public class JWTProvider implements InitializingBean {

    // Access Token 유효기간 (3분)
    private static final long ACCESS_TOKEN_EXPIRATION_SECONDS = 180;

    // Refresh Token 유효기간 (5분)
    private static final long REFRESH_TOKEN_EXPIRATION_SECONDS = 300;

    // JWT Claims 키 이름 정의
    private static final String PROVIDER_ID = "providerId";
    private static final String MEMBER_ID = "memberId";

    // base64 인코딩된 비밀키 (application.yml에서 주입)
    @Value("${jwt.key}")
    private String encodedKeyValue;

    private SecretKey key;
    private final Clock clock;

    // 애플리케이션 시작 시 JWT 서명용 키 생성
    @Override
    public void afterPropertiesSet() {
        key = buildKey();
    }

    // Base64 디코딩하여 HMAC SHA256 키 생성
    private SecretKey buildKey() {
        byte[] decodedKeyValue = Base64.getDecoder().decode(encodedKeyValue);
        return Keys.hmacShaKeyFor(decodedKeyValue);
    }

    // Access Token 발급
    // 사용 위치: 로그인, 회원가입
    public String buildAccessToken(String providerId, Long memberId) {
        return buildToken(providerId, memberId, ACCESS_TOKEN_EXPIRATION_SECONDS);
    }

    // Refresh Token 발급
    // 사용 위치: 로그인, 회원가입
    public String buildRefreshToken(String providerId, Long memberId) {
        return buildToken(providerId, memberId, REFRESH_TOKEN_EXPIRATION_SECONDS);
    }

    // 실제 JWT 생성 로직
    // 내부 공통 로직 (Access & Refresh 모두 여기서 만듦)
    private String buildToken(String providerId, Long memberId, long expirationSeconds) {
        Instant now = clock.instant();
        return Jwts.builder()
                .claim(PROVIDER_ID, providerId)
                .claim(MEMBER_ID, memberId)
                .signWith(key)
                .setIssuedAt(Date.from(now))
                .setExpiration(Date.from(now.plusSeconds(expirationSeconds)))
                .compact();
    }

    // 토큰 유효성 검사
    // 사용 위치: 필터(JwtFilter), Refresh API
    public boolean validateToken(String token) {
        try {
            Jws<Claims> claims = Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token);
            return !claims.getBody().getExpiration().before(new Date());
        } catch (JwtException e) {
            return false;
        }
    }

    // JWT payload 파싱 (claim 추출용)
    // 사용 위치: providerId, memberId 추출 시
    private Claims parsePayload(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(key)
                    .setClock(() -> Date.from(clock.instant()))
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (JwtException exception) {
            throw new InvalidJWTException("유효하지 않은 token 입니다.", exception);
        }
    }

    // JWT에서 providerId 추출
    // 사용 위치: 필터(JwtFilter), Refresh API
    public String getProviderId(String token) {
        Claims payload = parsePayload(token);
        return (String) payload.get(PROVIDER_ID);
    }

    // JWT에서 memberId 추출
    // 사용 위치: 필터(JwtFilter), Refresh API
    public Long getMemberId(String token) {
        Claims payload = parsePayload(token);
        return ((Number) payload.get(MEMBER_ID)).longValue();
    }
}

