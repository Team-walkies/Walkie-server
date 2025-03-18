package site.walkies.walkie.domain.member.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import site.walkies.walkie.domain.member.service.MemberService;
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
}
