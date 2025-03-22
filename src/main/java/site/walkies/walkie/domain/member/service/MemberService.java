package site.walkies.walkie.domain.member.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.walkies.walkie.domain.egg.entity.Egg;
import site.walkies.walkie.domain.egg.repository.EggRepository;
import site.walkies.walkie.domain.member.entity.Member;
import site.walkies.walkie.domain.member.repository.MemberRepository;
import site.walkies.walkie.domain.member.service.dto.request.MemberUpdateLevelingEggRequestDto;
import site.walkies.walkie.domain.member.service.dto.request.MemberUpdateRequestDto;
import site.walkies.walkie.domain.member.service.dto.response.MemberResponseDto;
import site.walkies.walkie.global.web.exception.CustomException;
import site.walkies.walkie.global.web.exception.ErrorCode;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    private final EggRepository eggRepository;

    // 사용자 정보 조회
    @Transactional(readOnly = true)
    public MemberResponseDto getMemberInfo(Long memberId){
        Member member = memberRepository.findById(memberId).orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
        return convertMemberToResponseDto(member);
    }

    // 사용자 정보 업데이트
    @Transactional
    public MemberResponseDto updateMemberInfo(Long memberId, MemberUpdateRequestDto memberUpdateRequestDto){
        Member member = memberRepository.findById(memberId).orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
        if(memberUpdateRequestDto.getMemberNickname() != null){
            member.changeNickname(memberUpdateRequestDto.getMemberNickname());
        }
        return convertMemberToResponseDto(member);
    }

    // 사용자와 함께 걷는 알 변경
    @Transactional
    public MemberResponseDto updateMemberLevelingEgg(Long memberId, MemberUpdateLevelingEggRequestDto memberUpdateLevelingEggRequestDto){
        Member member = memberRepository.findById(memberId).orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        Egg egg = eggRepository.findById(memberUpdateLevelingEggRequestDto.getEggId())
                .orElseThrow(() -> new CustomException(ErrorCode.EGG_NOT_FOUND));

        member.changeLevelingEgg(egg); // 또는 changeLevelingEgg(egg);

        return convertMemberToResponseDto(member);
    }

    // Member객체를 MemberResponse DTO로 변경
    public MemberResponseDto convertMemberToResponseDto(Member member){
        return MemberResponseDto.builder()
                .id(member.getId())
                .provider(member.getProvider())
                .nickname(member.getNickname())
                .isPublic(member.getIsPublic())
                .memberTier(member.getMemberTier())
                .eggId(member.getLevelingEgg() != null ? member.getLevelingEgg().getId() : null)
                .userCharacterId(member.getLevelingUserCharacter() != null ? member.getLevelingUserCharacter().getId() : null)
                .recordedSpot(member.getRecordedSpot())
                .exploredSpot(member.getExploredSpot())
                .build();
    }
}
