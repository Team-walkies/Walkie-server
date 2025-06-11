package site.walkies.walkie.domain.auth.strategy;

import org.springframework.stereotype.Component;
import site.walkies.walkie.domain.auth.service.AppleService;
import site.walkies.walkie.domain.member.service.MemberLoginService;
import site.walkies.walkie.domain.member.service.dto.response.MemberResponseDto;

@Component
public class AppleLoginStrategy implements SocialLoginStrategy {

    private final AppleService appleService;
    private final MemberLoginService memberLoginService;

    public AppleLoginStrategy(AppleService appleService, MemberLoginService memberLoginService) {
        this.appleService = appleService;
        this.memberLoginService = memberLoginService;
    }

    @Override
    public String getProviderName() {
        return "apple";
    }

    @Override
    public MemberResponseDto findMember(String token) {
        String appleUserId = appleService.getAppleUserIdFromToken(token);
        return memberLoginService.findAppleMember(appleUserId);
    }

    @Override
    public MemberResponseDto signup(String token, String nickname) {
        String appleUserId = appleService.getAppleUserIdFromToken(token);
        return memberLoginService.createAppleMember(appleUserId, nickname);
    }
}
