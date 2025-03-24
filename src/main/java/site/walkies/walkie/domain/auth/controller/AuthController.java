package site.walkies.walkie.domain.auth.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import site.walkies.walkie.domain.auth.service.AuthService;
import site.walkies.walkie.domain.auth.service.dto.request.LoginRequestDto;
import site.walkies.walkie.domain.auth.service.dto.response.LoginResponseDto;
import site.walkies.walkie.global.web.dto.response.SuccessResponse;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    @Operation(
            summary = "소셜 로그인 요청",
            description = "카카오 또는 애플 계정을 통한 소셜 로그인을 처리합니다. " +
                    "요청 본문에는 provider(kakao 또는 apple)와 각 플랫폼의 access token이 포함됩니다."
    )
    @PostMapping("/login")
    public SuccessResponse<LoginResponseDto> login(@RequestBody LoginRequestDto requestDto) {
        log.info("[AuthController] Received login request for provider: {}", requestDto.getProvider());

        // provider에 따라 로그인 처리
        LoginResponseDto loginResponseDto = authService.login(requestDto);

        return SuccessResponse.ok(loginResponseDto);
    }
}
