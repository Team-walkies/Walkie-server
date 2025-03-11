package site.walkies.walkie.domain.auth.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import site.walkies.walkie.domain.auth.service.dto.request.LoginRequestDto;
import site.walkies.walkie.domain.auth.service.dto.response.KakaoUserInfoResponseDto;
import site.walkies.walkie.domain.auth.service.dto.response.LoginResponseDto;
import site.walkies.walkie.domain.member.service.MemberLoginService;
import site.walkies.walkie.domain.member.service.dto.response.MemberResponseDto;
import site.walkies.walkie.global.auth.utils.JWTProvider;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final KakaoService kakaoService;
    private final AppleService appleService;
    private final MemberLoginService memberLoginService;
    private final JWTProvider jwtProvider;

    public LoginResponseDto login(LoginRequestDto requestDto) {
        MemberResponseDto memberResponseDto;

        switch (requestDto.getProvider().toLowerCase()) {
            case "kakao":
                memberResponseDto = loginWithKakao(requestDto.getLoginAccessToken());
                break;
            case "apple":
                memberResponseDto = loginWithApple(requestDto.getLoginAccessToken());
                break;
            default:
                throw new IllegalArgumentException("지원하지 않는 로그인 방식입니다.");
        }

        // JWT 토큰 생성
        String accessToken = jwtProvider.buildAccessToken(
                memberResponseDto.getProviderId(),
                memberResponseDto.getId()
        );

        return LoginResponseDto.builder()
                .provider(memberResponseDto.getProvider())
                .accessToken(accessToken)
                .refreshToken(null) // Refresh Token은 추후 구현
                .build();
    }

    private MemberResponseDto loginWithKakao(String accessToken) {
        KakaoUserInfoResponseDto userInfo = kakaoService.getUserInfo(accessToken);
        return memberLoginService.findOrCreateKakaoMember(userInfo);
    }

    private MemberResponseDto loginWithApple(String accessToken) {
        String appleUserId = appleService.verifyIdTokenAndGetUserId(accessToken);
        return memberLoginService.findOrCreateAppleMember(appleUserId);
    }
}
