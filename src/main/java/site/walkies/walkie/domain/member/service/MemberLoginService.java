package site.walkies.walkie.domain.member.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import site.walkies.walkie.domain.auth.service.dto.response.KakaoUserInfoResponseDto;
import site.walkies.walkie.domain.character.service.CharacterService;
import site.walkies.walkie.domain.member.repository.MemberRepository;
import site.walkies.walkie.domain.member.entity.Member;
import site.walkies.walkie.domain.member.service.dto.response.MemberResponseDto;
import site.walkies.walkie.global.web.exception.CustomException;
import site.walkies.walkie.global.web.exception.ErrorCode;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class MemberLoginService {

    private final MemberRepository memberRepository;
    private final CharacterService characterService;

    // 로그인 확인용 (회원 조회만)
    public MemberResponseDto findKakaoMember(KakaoUserInfoResponseDto userInfo) {
        String providerId = userInfo.getId().toString();
        return memberRepository.findByProviderId(providerId)
                .map(this::convertMemberToMemberResponseDto)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
    }

    public MemberResponseDto findAppleMember(String appleUserId) {
        return memberRepository.findByProviderId(appleUserId)
                .map(this::convertMemberToMemberResponseDto)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
    }

    // 카카오 회원가입 처리
    public MemberResponseDto createKakaoMember(KakaoUserInfoResponseDto userInfo, String nickname) {
        String providerId = userInfo.getId().toString();
        return createMember("kakao", providerId, nickname);
    }

    // 애플 회원가입 처리
    public MemberResponseDto createAppleMember(String appleUserId, String nickname) {
        return createMember("apple", appleUserId, nickname);
    }

    // memberId로 DB에서 정보 가져오기
    public Member findMemberById(Long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
    }

    // 멤버 생성
    private MemberResponseDto createMember(String provider, String providerId, String nickname) {
        // 기존에 있는 회원인지 조회
        boolean existsMember = memberRepository.existsByProviderAndProviderId(provider, providerId);

        if(existsMember) {
            throw new CustomException(ErrorCode.ALREADY_REGISTERED_USER);
        }

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
        newMember.changeLevelingUserCharacter(characterService.createDefaultCharacter(newMember.getId(), LocalDate.now()));
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
