package site.walkies.walkie.domain.auth.strategy;

import site.walkies.walkie.domain.member.service.dto.response.MemberResponseDto;

public interface SocialLoginStrategy {
    String getProviderName();
    MemberResponseDto findMember(String token); // 기존 getExistingMemberIfExists를 대체
    MemberResponseDto signup(String token, String nickname);
}