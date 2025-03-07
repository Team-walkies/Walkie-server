package site.walkies.walkie.domain.auth.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import site.walkies.walkie.domain.auth.service.AuthService;
import site.walkies.walkie.domain.auth.service.dto.request.LoginRequestDto;
import site.walkies.walkie.domain.member.service.dto.response.MemberResponseDto;
import site.walkies.walkie.global.web.dto.response.SuccessResponse;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public SuccessResponse<MemberResponseDto> login(@RequestBody LoginRequestDto requestDto) {
        log.info("[AuthController] Received login request for provider: {}", requestDto.getProvider());

        // provider에 따라 로그인 처리
        MemberResponseDto memberResponseDto = authService.login(requestDto);

        return SuccessResponse.ok(memberResponseDto);
    }
}
