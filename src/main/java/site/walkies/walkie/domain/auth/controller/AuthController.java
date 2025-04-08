package site.walkies.walkie.domain.auth.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import site.walkies.walkie.domain.auth.service.AuthService;
import site.walkies.walkie.domain.auth.service.dto.request.AuthCheckRequestDto;
import site.walkies.walkie.domain.auth.service.dto.request.AuthSignupRequestDto;
import site.walkies.walkie.domain.auth.service.dto.request.RefreshTokenRequestDto;
import site.walkies.walkie.domain.auth.service.dto.response.LoginResponseDto;
import site.walkies.walkie.domain.member.service.dto.response.MemberResponseDto;
import site.walkies.walkie.global.auth.dto.MemberPrincipal;
import site.walkies.walkie.global.web.dto.response.SuccessResponse;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    @Operation(
            summary = "소셜 로그인 요청",
            description = "카카오 또는 애플 계정으로 로그인 시도 시 회원 여부를 확인하고, 기존 회원이면 JWT를 발급합니다.\n\n" +
                    "- ✅ 기존 회원: accessToken 포함된 응답 반환 (로그인 성공)\n" +
                    "- ❌ 미가입 회원: accessToken=null 응답 반환 (회원가입 유도)\n" +
                    "- ⚠️ 유효하지 않은 토큰: 401 Unauthorized 응답 반환"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "로그인 결과 반환 (기존 회원 여부에 따라 accessToken 포함 또는 null)"),
            @ApiResponse(responseCode = "401", description = "유효하지 않은 소셜 토큰")
    })
    @PostMapping("/login")
    public SuccessResponse<LoginResponseDto> check(@RequestBody AuthCheckRequestDto requestDto) {
        log.info("[AuthController] Check login for provider: {}", requestDto.getProvider());
        LoginResponseDto loginResponseDto = authService.checkUserExists(requestDto);
        return SuccessResponse.ok(loginResponseDto);
    }

    @Operation(
            summary = "소셜 회원가입 요청",
            description = "카카오 또는 애플 계정으로 로그인한 사용자에 대해 닉네임을 설정하고 회원가입을 처리합니다.\n\n" +
                    "- ✅ 회원가입 성공: JWT 토큰 발급\n" +
                    "- ❌ 이미 가입된 유저: 409 Conflict\n" +
                    "- ❌ 닉네임 중복/형식 오류 등: 400 Bad Request\n" +
                    "- ⚠️ 유효하지 않은 토큰: 401 Unauthorized"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "회원가입 및 토큰 발급 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청 (닉네임 중복 등)"),
            @ApiResponse(responseCode = "401", description = "유효하지 않은 소셜 토큰"),
            @ApiResponse(responseCode = "409", description = "이미 가입된 계정")
    })
    @PostMapping("/signup")
    public SuccessResponse<LoginResponseDto> signup(@RequestBody AuthSignupRequestDto requestDto) {
        log.info("[AuthController] Signup request for provider: {}", requestDto.getProvider());
        LoginResponseDto loginResponseDto = authService.signupNewUser(requestDto);
        return SuccessResponse.ok(loginResponseDto);
    }

    @Operation(
            summary = "JWT 토큰 재발급",
            description = """
        저장된 Refresh Token을 사용해 Access Token과 새로운 Refresh Token을 재발급합니다.
        
        - 요청: `{ "refreshToken": "<발급받은 refresh token>" }`
        - 응답: `{ accessToken, refreshToken, provider }`
        
        📌 Refresh Token이 만료되었거나 존재하지 않는다면 401 에러가 발생합니다.
        """
    )
    @PostMapping("/refresh")
    public SuccessResponse<LoginResponseDto> refreshToken(@RequestBody RefreshTokenRequestDto requestDto) {
        LoginResponseDto response = authService.refreshAccessToken(requestDto);
        return SuccessResponse.ok(response);
    }


    @Operation(
            summary = "사용자 로그아웃",
            description = "사용자의 refresh token 정보를 삭제하여 로그아웃 시킵니다."
    )
    @PostMapping("/logout")
    public SuccessResponse<MemberResponseDto> logout(@AuthenticationPrincipal MemberPrincipal memberPrincipal) {
        MemberResponseDto memberResponseDto = authService.logout(memberPrincipal.getMemberId());
        return SuccessResponse.ok(memberResponseDto);
    }

}
