package site.walkies.walkie.domain.auth.service.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class LoginResponseDto {
    private String provider;
    private String accessToken;
    private String refreshToken;

    @Builder
    public LoginResponseDto(String provider, String accessToken, String refreshToken) {
        this.provider = provider;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }
}
