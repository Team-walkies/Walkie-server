package site.walkies.walkie.domain.member.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import site.walkies.walkie.domain.egg.service.dto.response.EggResponse;
import site.walkies.walkie.domain.character.service.dto.response.GetCharacterResponse;
import site.walkies.walkie.domain.member.service.MemberDeletionService;
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
    private final MemberDeletionService memberDeletionService;

    @Operation(
            summary = "사용자 정보 조회",
            description = "현재 로그인한 사용자의 정보를 조회합니다."
    )
    @GetMapping
    public SuccessResponse<MemberResponseDto> getMemberInfo(@AuthenticationPrincipal MemberPrincipal memberPrincipal ) {
        MemberResponseDto memberResponseDto = memberService.getMemberInfo(memberPrincipal.getMemberId());
        return SuccessResponse.ok(memberResponseDto);
    }

    @Operation(
            summary = "사용자 정보 수정",
            description = "사용자의 프로필 정보를 수정합니다.(현재는 닉네임만 가능)"
    )
    @PatchMapping
    public SuccessResponse<MemberResponseDto> updateMemberInfo(@AuthenticationPrincipal MemberPrincipal memberPrincipal, @RequestBody MemberUpdateRequestDto memberUpdateRequestDto ) {
        MemberResponseDto memberResponseDto = memberService.updateMemberInfo(memberPrincipal.getMemberId(), memberUpdateRequestDto);
        return SuccessResponse.updated(memberResponseDto);
    }

    @Operation(
            summary = "함께 걷는 캐릭터 조회",
            description = "사용자가 설정한 '함께 걷는 캐릭터' 정보를 조회합니다."
    )
    @GetMapping("/characters/play")
    public SuccessResponse<GetCharacterResponse> getMemberCharacter(@AuthenticationPrincipal MemberPrincipal memberPrincipal) {
        GetCharacterResponse getCharacterResponse = memberService.getMemberCharacter(memberPrincipal.getMemberId());
        return SuccessResponse.ok(getCharacterResponse);
    }

    @Operation(
            summary = "함께 걷는 캐릭터 변경",
            description = "사용자의 '함께 걷는 캐릭터'를 변경합니다."
    )
    @PatchMapping("/characters/play")
    public SuccessResponse<MemberResponseDto> updateMemberLevelingCharacter(@AuthenticationPrincipal MemberPrincipal memberPrincipal, @RequestBody MemberUpdateCharacterRequestDto memberUpdateCharacterRequestDto ) {
        MemberResponseDto memberResponseDto = memberService.updateMemberLevelingCharacter(memberPrincipal.getMemberId(), memberUpdateCharacterRequestDto);
        return SuccessResponse.updated(memberResponseDto);
    }

    @Operation(
            summary = "부화 중인 알 조회",
            description = "사용자가 현재 부화 중인 알 정보를 조회합니다."
    )
    @GetMapping("/eggs/play")
    public SuccessResponse<EggResponse> getMemberLevelingEgg(@AuthenticationPrincipal MemberPrincipal memberPrincipal){
        EggResponse eggResponse = memberService.getMemberLevelingEgg(memberPrincipal.getMemberId());
        return SuccessResponse.ok(eggResponse);
    }

    @Operation(
            summary = "부화 중인 알 변경",
            description = "사용자의 현재 부화 중인 알을 변경합니다."
    )
    @PatchMapping("/eggs/play")
    public SuccessResponse<EggResponse> updateMemberLevelingEgg(@AuthenticationPrincipal MemberPrincipal memberPrincipal, @RequestBody MemberUpdateLevelingEggRequestDto memberUpdateLevelingEggRequestDto){
        EggResponse eggResponse = memberService.updateMemberLevelingEgg(memberPrincipal.getMemberId(), memberUpdateLevelingEggRequestDto);
        return SuccessResponse.updated(eggResponse);
    }

    @Operation(
            summary = "사용자 프로필 공개 여부 토글",
            description = "사용자의 프로필을 공개/비공개로 전환합니다."
    )
    @PatchMapping("/profile/visibility")
    public SuccessResponse<MemberResponseDto> toggleMemberProfileVisibility(@AuthenticationPrincipal MemberPrincipal memberPrincipal) {
        MemberResponseDto memberResponseDto = memberService.toggleMemberProfileVisibility(memberPrincipal.getMemberId());
        return SuccessResponse.updated(memberResponseDto);
    }

    @Operation(
            summary = "사용자의 기록한 스팟 개수 조회",
            description = "메인 화면에서 사용자의 기록한 스팟 개수를 조회할 때 사용합니다."
    )
    @GetMapping("/recorded-spot")
    public SuccessResponse<Integer> getMemberRecordedSpot(@AuthenticationPrincipal MemberPrincipal memberPrincipal) {
        Integer memberRecordedSpot = memberService.getMemberRecordedSpot(memberPrincipal.getMemberId());
        return SuccessResponse.ok(memberRecordedSpot);
    }

    @Operation(
            summary = "회원 탈퇴",
            description = "회원 탈퇴 요청을 보낼 때 사용합니다."
    )
    @DeleteMapping("")
    public SuccessResponse<Long> deleteMember(@AuthenticationPrincipal MemberPrincipal memberPrincipal) {
        Long deletedId = memberDeletionService.softDeleteMember(memberPrincipal.getMemberId());
        return SuccessResponse.deleted(deletedId);
    }
}

