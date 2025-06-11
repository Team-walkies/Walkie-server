package site.walkies.walkie.domain.auth.strategy;

import org.springframework.stereotype.Component;
import site.walkies.walkie.domain.auth.service.KakaoService;
import site.walkies.walkie.domain.auth.service.dto.response.KakaoUserInfoResponseDto;
import site.walkies.walkie.domain.member.service.MemberLoginService;
import site.walkies.walkie.domain.member.service.dto.response.MemberResponseDto;

@Component
public class KakaoLoginStrategy implements SocialLoginStrategy {

    private final KakaoService kakaoService;
    private final MemberLoginService memberLoginService;

    public KakaoLoginStrategy(KakaoService kakaoService, MemberLoginService memberLoginService) {
        this.kakaoService = kakaoService;
        this.memberLoginService = memberLoginService;
    }

    @Override
    public String getProviderName() {
        return "kakao";
    }

    @Override
    public MemberResponseDto findMember(String token) {
        KakaoUserInfoResponseDto userInfo = kakaoService.getUserInfo(token);
        return memberLoginService.findKakaoMember(userInfo);
    }

    @Override
    public MemberResponseDto signup(String token, String nickname) {
        KakaoUserInfoResponseDto userInfo = kakaoService.getUserInfo(token);
        return memberLoginService.createKakaoMember(userInfo, nickname);
    }
}
