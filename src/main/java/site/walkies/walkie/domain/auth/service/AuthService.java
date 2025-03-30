package site.walkies.walkie.domain.auth.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import site.walkies.walkie.domain.auth.service.dto.request.AuthCheckRequestDto;
import site.walkies.walkie.domain.auth.service.dto.request.AuthSignupRequestDto;
import site.walkies.walkie.domain.auth.service.dto.response.KakaoUserInfoResponseDto;
import site.walkies.walkie.domain.auth.service.dto.response.LoginResponseDto;
import site.walkies.walkie.domain.member.service.MemberLoginService;
import site.walkies.walkie.domain.member.service.dto.response.MemberResponseDto;
import site.walkies.walkie.global.auth.utils.JWTProvider;
import site.walkies.walkie.global.web.exception.CustomException;
import site.walkies.walkie.global.web.exception.ErrorCode;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final KakaoService kakaoService;
    private final AppleService appleService;
    private final MemberLoginService memberLoginService;
    private final JWTProvider jwtProvider;

    public LoginResponseDto checkUserExists(AuthCheckRequestDto requestDto) {
        MemberResponseDto memberResponseDto = getExistingMemberIfExists(requestDto);

        if (memberResponseDto == null) {
            // 회원이 없는 경우: 프론트에서 회원가입 유도
            return LoginResponseDto.builder()
                    .provider(requestDto.getProvider())
                    .accessToken(null)
                    .refreshToken(null)
                    .build();
        }

        String accessToken = jwtProvider.buildAccessToken(
                memberResponseDto.getProviderId(),
                memberResponseDto.getId()
        );

        return LoginResponseDto.builder()
                .provider(memberResponseDto.getProvider())
                .accessToken(accessToken)
                .refreshToken(null)
                .build();
    }

    public LoginResponseDto signupNewUser(AuthSignupRequestDto requestDto) {
        MemberResponseDto memberResponseDto;

        switch (requestDto.getProvider().toLowerCase()) {
            case "kakao":
                KakaoUserInfoResponseDto kakaoUserInfo = kakaoService.getUserInfo(requestDto.getLoginAccessToken());
                memberResponseDto = memberLoginService.createKakaoMember(kakaoUserInfo, requestDto.getNickname());
                break;
            case "apple":
                String appleUserId = appleService.getAppleUserIdFromToken(requestDto.getLoginAccessToken());
                memberResponseDto = memberLoginService.createAppleMember(appleUserId, requestDto.getNickname());
                break;
            default:
                throw new IllegalArgumentException("지원하지 않는 로그인 방식입니다.");
        }

        String accessToken = jwtProvider.buildAccessToken(
                memberResponseDto.getProviderId(),
                memberResponseDto.getId()
        );

        return LoginResponseDto.builder()
                .provider(memberResponseDto.getProvider())
                .accessToken(accessToken)
                .refreshToken(null)
                .build();
    }

    private MemberResponseDto getExistingMemberIfExists(AuthCheckRequestDto requestDto) {
        try {
            switch (requestDto.getProvider().toLowerCase()) {
                case "kakao":
                    KakaoUserInfoResponseDto kakaoUserInfo = kakaoService.getUserInfo(requestDto.getLoginAccessToken());
                    return memberLoginService.findKakaoMember(kakaoUserInfo);
                case "apple":
                    String appleUserId = appleService.getAppleUserIdFromToken(requestDto.getLoginAccessToken());
                    return memberLoginService.findAppleMember(appleUserId);
                default:
                    throw new IllegalArgumentException("지원하지 않는 로그인 방식입니다.");
            }
        } catch (CustomException e) {
            if (e.getErrorCode() == ErrorCode.USER_NOT_FOUND) {
                return null;
            }
            throw e;
        }
    }
}
