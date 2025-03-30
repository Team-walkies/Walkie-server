package site.walkies.walkie.domain.auth.service.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class AuthCheckRequestDto {
    private String provider;  // "kakao" 또는 "apple"
    private String loginAccessToken;
}