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

@RequiredArgsConstructor
@Component
@Slf4j
public class JWTProvider implements InitializingBean {

    private static final int ACCESS_TOKEN_EXPIRATION_PERIOD = 60 * 60 * 100;
    private static final String PROVIDER_ID = "providerId";
    private static final String MEMBER_ID = "memberId";

    @Value("${jwt.key}")
    private String encodedKeyValue;

    private SecretKey key;
    private final Clock clock;

    @Override
    public void afterPropertiesSet() throws Exception {
        key = buildKey();
    }

    private SecretKey buildKey() {
        byte[] decodedKeyValue = Base64.getDecoder().decode(encodedKeyValue);
        return Keys.hmacShaKeyFor(decodedKeyValue);
    }

    public String buildAccessToken(String providerId, Long memberId) {
        Instant now = clock.instant();

        return Jwts.builder()
                .claim(PROVIDER_ID, providerId)
                .claim(MEMBER_ID, memberId)
                .signWith(key)
                .setIssuedAt(Date.from(now))
                .setExpiration(Date.from(now.plusSeconds(ACCESS_TOKEN_EXPIRATION_PERIOD)))
                .compact();
    }

    public boolean validateToken(String token) {
        Jws<Claims> claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token);
        return !claims.getBody().getExpiration().before(new Date());
    }

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

    public String getProviderId(String token) {
        Claims payload = parsePayload(token);
        return (String) payload.get(PROVIDER_ID);
    }

    public Long getMemberId(String token) {
        Claims payload = parsePayload(token);
        return ((Number) payload.get(MEMBER_ID)).longValue();
    }
}
