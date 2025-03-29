package site.walkies.walkie.global.auth.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import site.walkies.walkie.global.auth.dto.ApplePublicKeyResponse;
import site.walkies.walkie.global.auth.dto.ApplePublicKeyResponse.ApplePublicKey;

import java.io.IOException;
import java.net.URL;
import java.security.interfaces.RSAPublicKey;
import java.util.Base64;
import java.util.Optional;

@Slf4j
public class AppleKeyUtils {

    private static final String APPLE_PUBLIC_KEYS_URL = "https://appleid.apple.com/auth/keys";

    public static RSAPublicKey getApplePublicKeyByKid(String kid) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            ApplePublicKeyResponse keyResponse = objectMapper.readValue(new URL(APPLE_PUBLIC_KEYS_URL), ApplePublicKeyResponse.class);

            Optional<ApplePublicKey> matchingKey = keyResponse.getKeys().stream()
                    .filter(key -> key.getKid().equals(kid))
                    .findFirst();

            if (matchingKey.isEmpty()) {
                throw new IllegalArgumentException("일치하는 Apple 공개키(kid: " + kid + ")를 찾을 수 없습니다.");
            }

            return matchingKey.get().toRSAPublicKey();
        } catch (IOException e) {
            throw new RuntimeException("Apple 공개키를 가져오는 중 오류 발생", e);
        }
    }

    public static String extractKidFromToken(String idToken) {
        try {
            String[] parts = idToken.split("\\.");
            if (parts.length < 2) {
                throw new IllegalArgumentException("유효하지 않은 JWT 형식입니다.");
            }

            String headerJson = new String(Base64.getUrlDecoder().decode(parts[0]));
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readTree(headerJson).get("kid").asText();
        } catch (Exception e) {
            throw new RuntimeException("JWT header에서 kid를 추출하는 중 오류 발생", e);
        }
    }
}
