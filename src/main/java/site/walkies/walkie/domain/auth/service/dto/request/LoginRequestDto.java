package site.walkies.walkie.domain.auth.service.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class LoginRequestDto {
    private String provider;  // "kakao" 또는 "apple"
    private String accessToken;  // 카카오 accessToken 또는 애플 idToken
}

