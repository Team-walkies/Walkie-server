package site.walkies.walkie.domain.member.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import site.walkies.walkie.domain.auth.service.dto.response.KakaoUserInfoResponseDto;
import site.walkies.walkie.domain.member.repository.MemberRepository;
import site.walkies.walkie.domain.member.entity.Member;
import site.walkies.walkie.domain.member.service.dto.response.MemberResponseDto;

@Service
@RequiredArgsConstructor
public class MemberLoginService {

    private final MemberRepository memberRepository;

    public MemberResponseDto findOrCreateKakaoMember(KakaoUserInfoResponseDto userInfo) {
        String providerId = userInfo.getId().toString();
        return findOrCreateMember("kakao", providerId, userInfo.getKakaoAccount().getProfile().getNickName());
    }

    public MemberResponseDto findOrCreateAppleMember(String appleUserId) {
        return findOrCreateMember("apple", appleUserId, "애플 사용자");
    }

    // 기존 멤버인지, 아닌지 확인
    private MemberResponseDto findOrCreateMember(String provider, String providerId, String nickname) {
        return memberRepository.findByProviderId(providerId)
                .map(this::convertMemberToMemberResponseDto)
                .orElseGet(() -> createMember(provider, providerId, nickname));
    }

    // 멤버 생성
    private MemberResponseDto createMember(String provider, String providerId, String nickname) {
        Member newMember = Member.builder()
                .provider(provider)
                .providerId(providerId)
                .nickname(nickname)
                .exploredSpot(0)
                .recordedSpot(0)
                .isPublic(true)
                .memberTier("초보워키")
                .levelingEgg(null)
                .levelingUserCharacter(null)
                .build();
        Member savedMember = memberRepository.save(newMember);
        return convertMemberToMemberResponseDto(savedMember);
    }

    private MemberResponseDto convertMemberToMemberResponseDto(Member member) {
        return MemberResponseDto.builder()
                .id(member.getId())
                .providerId(member.getProviderId())
                .provider(member.getProvider())
                .nickname(member.getNickname())
                .build();
    }

}
