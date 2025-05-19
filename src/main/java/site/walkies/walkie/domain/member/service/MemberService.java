package site.walkies.walkie.domain.member.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.walkies.walkie.domain.egg.entity.Egg;
import site.walkies.walkie.domain.egg.repository.EggRepository;
import site.walkies.walkie.domain.egg.service.dto.response.EggResponse;
import site.walkies.walkie.domain.character.entity.UserCharacter;
import site.walkies.walkie.domain.character.service.dto.response.GetCharacterResponse;
import site.walkies.walkie.domain.character.repository.UserCharacterRepository;
import site.walkies.walkie.domain.member.entity.Member;
import site.walkies.walkie.domain.member.repository.MemberRepository;
import site.walkies.walkie.domain.member.service.dto.request.MemberUpdateCharacterRequestDto;
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
    private final UserCharacterRepository characterRepository;

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

    // 사용자가 부화시키는 알 변경
    @Transactional
    public EggResponse updateMemberLevelingEgg(Long memberId, MemberUpdateLevelingEggRequestDto memberUpdateLevelingEggRequestDto){
        Member member = memberRepository.findById(memberId).orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
        Egg egg = eggRepository.findById(memberUpdateLevelingEggRequestDto.getEggId())
                .orElseThrow(() -> new CustomException(ErrorCode.EGG_NOT_FOUND));

        Egg previousEgg = member.getLevelingEgg();
        // 부화시키는 알이 null인 경우나, 이전과 같은 알을 선택한 경우(프론트 오류 등으로 인해)는 체크 따로 안하기.
        if (previousEgg != null && !previousEgg.getId().equals(egg.getId())) {
            previousEgg.changePicked(false);
        }

        member.changeLevelingEgg(egg);
        egg.changePicked(true);


        return EggResponse.builder()
                .eggId(egg.getId())
                .rank(egg.getRank())
                .needStep(egg.getNeedStep())
                .nowStep(egg.getNowStep())
                .obtainedPosition(egg.getObtainedPosition())
                .obtainedDate(egg.getObtainedDate())
                .picked(egg.getPicked())
                .userCharacterId(egg.getUserCharacter().getId())
                .characterRank(egg.getUserCharacter().getRank())
                .characterType(egg.getUserCharacter().getType())
                .characterClass(egg.getUserCharacter().getCharacterClass())
                .memberId(memberId)
                .build();
    }
    

    // 함께 걷는 캐릭터 변경
    @Transactional
    public MemberResponseDto updateMemberLevelingCharacter(Long memberId, MemberUpdateCharacterRequestDto memberUpdateCharacterRequestDto){
        Member member = memberRepository.findById(memberId).orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        UserCharacter character = characterRepository.findById(memberUpdateCharacterRequestDto.getCharacterId())
                .orElseThrow(() -> new CustomException(ErrorCode.CHARACTER_NOT_FOUND));

        UserCharacter previousCharacter = member.getLevelingUserCharacter();
        if(previousCharacter != null && !previousCharacter.getId().equals(character.getId())) {
            previousCharacter.changePicked(false);
        }

        member.changeLevelingUserCharacter(character);
        character.changePicked(true);

        return convertMemberToResponseDto(member);
    }

    // 같이 걷는 캐릭터 조회
    @Transactional(readOnly = true)
    public GetCharacterResponse getMemberCharacter(Long memberId){
        Member member  = memberRepository.findById(memberId).orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
        UserCharacter character = member.getLevelingUserCharacter();
        return GetCharacterResponse.builder()
                .characterId(character.getId())
                .type(character.getType())
                .characterClass(character.getCharacterClass())
                .rank(character.getRank())
                .picked(character.getPicked())
                .build();
    }

    // 사용자가 부화시키는 알 조회
    @Transactional(readOnly = true)
    public EggResponse getMemberLevelingEgg(Long memberId){
        Member member = memberRepository.findById(memberId).orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        Egg egg = member.getLevelingEgg();

        if (egg == null) {
            return EggResponse.builder()
                    .memberId(memberId)
                    .build();
        }

        return EggResponse.builder()
                .eggId(egg.getId())
                .rank(egg.getRank())
                .needStep(egg.getNeedStep())
                .nowStep(egg.getNowStep())
                .obtainedPosition(egg.getObtainedPosition())
                .obtainedDate(egg.getObtainedDate())
                .picked(egg.getPicked())
                .userCharacterId(egg.getUserCharacter().getId())
                .characterRank(egg.getUserCharacter().getRank())
                .characterType(egg.getUserCharacter().getType())
                .characterClass(egg.getUserCharacter().getCharacterClass())
                .memberId(memberId)
                .build();
    }

    @Transactional
    public MemberResponseDto toggleMemberProfileVisibility(Long memberId) {
        Member member = memberRepository.findById(memberId).orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
        member.changeProfileVisibility(!member.getIsPublic());
        return convertMemberToResponseDto(member);
    }

    // Member객체를 MemberResponse DTO로 변경
    public MemberResponseDto convertMemberToResponseDto(Member member){
        return MemberResponseDto.builder()
                .id(member.getId())
                .providerId(member.getProviderId())
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

    // 멤버가 기록한 스팟 개수 조회
    public Integer getMemberRecordedSpot(Long memberId) {
        Member member = memberRepository.findById(memberId).orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
        return member.getRecordedSpot();
    }
}
