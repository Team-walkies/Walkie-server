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

    // 기존 멤버인지, 아닌지 확인
    public MemberResponseDto findOrCreateMember(KakaoUserInfoResponseDto userInfo) {
        String providerId = userInfo.getId().toString();
        return memberRepository.findByProviderId(providerId)
                .map(this::converMemberToMemberResponseDto)
                .orElseGet(() -> createMember(userInfo));
    }

    // 멤버 생성
    private MemberResponseDto createMember(KakaoUserInfoResponseDto userInfo) {
        Member signUpInfo = createKakaoSignUpInfo(userInfo);
        Member savedMember = memberRepository.save(signUpInfo);
        return converMemberToMemberResponseDto(savedMember);
    }

    private Member createKakaoSignUpInfo(KakaoUserInfoResponseDto userInfo) {
        return Member.builder()
                .provider("kakao")
                .providerId(userInfo.getId().toString())
                .nickname(userInfo.getKakaoAccount().getProfile().getNickName())
                .exploredSpot(0)
                .recordedSpot(0)
                .isPublic(true)
                .memberTier("초보워키")
                .levelingEgg(null)
                .levelingUserCharacter(null)
                .build();
    }

    private MemberResponseDto converMemberToMemberResponseDto(Member member) {
        return MemberResponseDto.builder()
                .id(member.getId())
                .providerId(member.getProviderId())
                .provider(member.getProvider())
                .nickname(member.getNickname())
                .exploredSpot(member.getExploredSpot())
                .recordedSpot(member.getRecordedSpot())
                .memberTier(member.getMemberTier())
                .eggId(member.getLevelingEgg() != null ? member.getLevelingEgg().getId() : null)
                .userCharacterId(member.getLevelingUserCharacter() != null ? member.getLevelingUserCharacter().getId() : null)
                .build();
    }

}
