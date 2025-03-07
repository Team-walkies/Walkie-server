package site.walkies.walkie.domain.auth.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import site.walkies.walkie.domain.auth.service.dto.request.LoginRequestDto;
import site.walkies.walkie.domain.auth.service.dto.response.KakaoUserInfoResponseDto;
import site.walkies.walkie.domain.member.service.MemberLoginService;
import site.walkies.walkie.domain.member.service.dto.response.MemberResponseDto;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final KakaoService kakaoService;
    private final AppleService appleService;
    private final MemberLoginService memberLoginService;

    public MemberResponseDto login(LoginRequestDto requestDto) {
        switch (requestDto.getProvider().toLowerCase()) {
            case "kakao":
                return loginWithKakao(requestDto.getAccessToken());
            case "apple":
                return loginWithApple(requestDto.getAccessToken());
            default:
                throw new IllegalArgumentException("지원하지 않는 로그인 방식입니다.");
        }
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
