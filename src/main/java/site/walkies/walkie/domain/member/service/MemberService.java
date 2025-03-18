package site.walkies.walkie.domain.member.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import site.walkies.walkie.domain.member.entity.Member;
import site.walkies.walkie.domain.member.repository.MemberRepository;
import site.walkies.walkie.domain.member.service.dto.response.MemberResponseDto;
import site.walkies.walkie.global.web.exception.CustomException;
import site.walkies.walkie.global.web.exception.ErrorCode;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;

    // 사용자 정보 조회
    public MemberResponseDto getMemberInfo(Long memberId){
        Member member = memberRepository.findById(memberId).orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        return MemberResponseDto.builder()
                .id(member.getId())
                .provider(member.getProvider())
                .nickname(member.getNickname())
                .isPublic(member.getIsPublic())
                .memberTier(member.getMemberTier())
                .eggId(member.getLevelingEgg().getId())
                .userCharacterId(member.getLevelingUserCharacter().getId())
                .recordedSpot(member.getRecordedSpot())
                .exploredSpot(member.getExploredSpot())
                .build();
    }
}
