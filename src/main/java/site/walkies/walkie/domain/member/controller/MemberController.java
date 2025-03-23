package site.walkies.walkie.domain.member.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import site.walkies.walkie.domain.egg.service.dto.response.EggResponse;
import site.walkies.walkie.domain.egg.service.dto.response.GetEggResponse;
import site.walkies.walkie.domain.character.service.dto.response.GetCharacterResponse;
import site.walkies.walkie.domain.member.service.MemberService;
import site.walkies.walkie.domain.member.service.dto.request.MemberUpdateCharacterRequestDto;
import site.walkies.walkie.domain.member.service.dto.request.MemberUpdateLevelingEggRequestDto;
import site.walkies.walkie.domain.member.service.dto.request.MemberUpdateRequestDto;
import site.walkies.walkie.domain.member.service.dto.response.MemberResponseDto;
import site.walkies.walkie.global.auth.dto.MemberPrincipal;
import site.walkies.walkie.global.web.dto.response.SuccessResponse;

@RestController
@RequiredArgsConstructor
@RequestMapping("/members")
public class MemberController {

    private final MemberService memberService;

    // 사용자 정보 조회
    @GetMapping
    public SuccessResponse<MemberResponseDto> getMemberInfo(@AuthenticationPrincipal MemberPrincipal memberPrincipal ) {
        MemberResponseDto memberResponseDto = memberService.getMemberInfo(memberPrincipal.getMemberId());
        return SuccessResponse.ok(memberResponseDto);
    }

    // 사용자 정보 수정
    @PatchMapping
    public SuccessResponse<MemberResponseDto> updateMemberInfo(@AuthenticationPrincipal MemberPrincipal memberPrincipal, @RequestBody MemberUpdateRequestDto memberUpdateRequestDto ) {
        MemberResponseDto memberResponseDto = memberService.updateMemberInfo(memberPrincipal.getMemberId(), memberUpdateRequestDto);
        return SuccessResponse.updated(memberResponseDto);
    }

    // 함께 걷는 캐릭터 조회
    @GetMapping("/characters/play")
    public SuccessResponse<GetCharacterResponse> getMemberCharacter(@AuthenticationPrincipal MemberPrincipal memberPrincipal) {
        GetCharacterResponse getCharacterResponse = memberService.getMemberCharacter(memberPrincipal.getMemberId());
        return SuccessResponse.ok(getCharacterResponse);
    }
  
    // 사용자가 부화 시키는 알 변경
    @PatchMapping("/eggs/play")
    public SuccessResponse<MemberResponseDto> updateMemberLevelingEgg(@AuthenticationPrincipal MemberPrincipal memberPrincipal, @RequestBody MemberUpdateLevelingEggRequestDto memberUpdateLevelingEggRequestDto){
        MemberResponseDto memberResponseDto = memberService.updateMemberLevelingEgg(memberPrincipal.getMemberId(), memberUpdateLevelingEggRequestDto);
        return SuccessResponse.updated(memberResponseDto);
    }

    // 사용자가 부화시키는 알 조회
    @GetMapping("/eggs/play")
    public SuccessResponse<EggResponse> getMemberLevelingEgg(@AuthenticationPrincipal MemberPrincipal memberPrincipal){
        EggResponse eggResponse = memberService.getMemberLevelingEgg(memberPrincipal.getMemberId());
        return SuccessResponse.ok(eggResponse);
    }

    // 함께 걷는 캐릭터 변경
    @PatchMapping("/characters/play")
    public SuccessResponse<MemberResponseDto> updateMemberLevelingCharacter(@AuthenticationPrincipal MemberPrincipal memberPrincipal, @RequestBody MemberUpdateCharacterRequestDto memberUpdateCharacterRequestDto ) {
       MemberResponseDto memberResponseDto = memberService.updateMemberLevelingCharacter(memberPrincipal.getMemberId(), memberUpdateCharacterRequestDto);
       return SuccessResponse.updated(memberResponseDto);
    }
  
    // 사용자 프로필 조회 토글
    @PatchMapping("/profile/visibility")
    public SuccessResponse<MemberResponseDto> toggleMemberProfileVisibility(@AuthenticationPrincipal MemberPrincipal memberPrincipal) {
        MemberResponseDto memberResponseDto = memberService.toggleMemberProfileVisibility(memberPrincipal.getMemberId());
        return SuccessResponse.updated(memberResponseDto);
    }
}
