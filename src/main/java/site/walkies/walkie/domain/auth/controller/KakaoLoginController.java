package site.walkies.walkie.domain.auth.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import site.walkies.walkie.domain.auth.service.KakaoService;
import site.walkies.walkie.domain.auth.service.dto.response.KakaoUserInfoResponseDto;
import site.walkies.walkie.domain.member.service.MemberLoginService;
import site.walkies.walkie.domain.member.service.dto.response.MemberResponseDto;
import site.walkies.walkie.global.web.dto.response.SuccessResponse;

import java.io.IOException;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("")
public class KakaoLoginController {

    private final KakaoService kakaoService;
    private final MemberLoginService memberLoginService;

    @GetMapping("/callback")
    public SuccessResponse<MemberResponseDto> callback(@RequestParam("code") String code) throws IOException {
        log.info("[KakaoLoginController] Received callback request with code: {}", code);

        String accessToken = kakaoService.getAccessTokenFromKakao(code);
        log.info("[KakaoLoginController] Kakao access token: {}", accessToken);

        KakaoUserInfoResponseDto userInfo = kakaoService.getUserInfo(accessToken);
        log.info("[KakaoLoginController] Kakao user info: {}", userInfo);

        MemberResponseDto memberResponseDto = memberLoginService.findOrCreateMember(userInfo);
        log.info("[KakaoLoginController] Member response DTO: {}", memberResponseDto);


        return SuccessResponse.created(memberResponseDto);
    }
}