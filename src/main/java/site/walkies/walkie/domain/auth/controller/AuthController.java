package site.walkies.walkie.domain.auth.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import site.walkies.walkie.domain.auth.service.AuthService;
import site.walkies.walkie.domain.auth.service.dto.request.AuthCheckRequestDto;
import site.walkies.walkie.domain.auth.service.dto.request.AuthSignupRequestDto;
import site.walkies.walkie.domain.auth.service.dto.response.LoginResponseDto;
import site.walkies.walkie.global.web.dto.response.SuccessResponse;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    @Operation(summary = "소셜 로그인 여부 확인", description = "카카오 또는 애플 accessToken을 기반으로 기존 회원 여부를 확인합니다.")
    @PostMapping("/check")
    public SuccessResponse<LoginResponseDto> check(@RequestBody AuthCheckRequestDto requestDto) {
        log.info("[AuthController] Check login for provider: {}", requestDto.getProvider());
        LoginResponseDto loginResponseDto = authService.checkUserExists(requestDto);
        return SuccessResponse.ok(loginResponseDto);
    }

    @Operation(summary = "소셜 회원가입", description = "카카오 또는 애플 accessToken과 nickname 등을 받아 회원가입을 처리합니다.")
    @PostMapping("/signup")
    public SuccessResponse<LoginResponseDto> signup(@RequestBody AuthSignupRequestDto requestDto) {
        log.info("[AuthController] Signup request for provider: {}", requestDto.getProvider());
        LoginResponseDto loginResponseDto = authService.signupNewUser(requestDto);
        return SuccessResponse.ok(loginResponseDto);
    }
}
