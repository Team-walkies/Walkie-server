package site.walkies.walkie.domain.auth.service.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class AuthSignupRequestDto {
    private String provider;           // "kakao" 또는 "apple"
    private String loginAccessToken;   // accessToken 또는 id_token
    private String nickname;           // 사용자 입력 닉네임
}
