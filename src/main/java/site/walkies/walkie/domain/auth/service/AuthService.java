package site.walkies.walkie.domain.auth.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import site.walkies.walkie.domain.auth.service.dto.request.AuthCheckRequestDto;
import site.walkies.walkie.domain.auth.service.dto.request.AuthSignupRequestDto;
import site.walkies.walkie.domain.auth.service.dto.request.RefreshTokenRequestDto;
import site.walkies.walkie.domain.auth.service.dto.response.KakaoUserInfoResponseDto;
import site.walkies.walkie.domain.auth.service.dto.response.LoginResponseDto;
import site.walkies.walkie.domain.member.entity.Member;
import site.walkies.walkie.domain.member.service.MemberLoginService;
import site.walkies.walkie.domain.member.service.dto.response.MemberResponseDto;
import site.walkies.walkie.domain.member.token.MemberRefreshToken;
import site.walkies.walkie.domain.member.token.MemberRefreshTokenService;
import site.walkies.walkie.global.auth.utils.JWTProvider;
import site.walkies.walkie.global.web.exception.CustomException;
import site.walkies.walkie.global.web.exception.ErrorCode;

import java.time.LocalDateTime;

@Service
@Slf4j
@RequiredArgsConstructor
public class AuthService {

    private final KakaoService kakaoService;
    private final AppleService appleService;
    private final MemberLoginService memberLoginService;
    private final JWTProvider jwtProvider;
    private final MemberRefreshTokenService memberRefreshTokenService;

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

        // 토큰 발급
        String accessToken = jwtProvider.buildAccessToken(
                memberResponseDto.getProviderId(),
                memberResponseDto.getId()
        );
        String refreshToken = jwtProvider.buildRefreshToken(memberResponseDto.getProviderId(), memberResponseDto.getId());

        // refreshToken 저장
        saveRefreshToken(memberResponseDto.getId(), refreshToken);

        log.info("ID: {} 인 사용자가 로그인 했습니다.", memberResponseDto.getId());

        return LoginResponseDto.builder()
                .provider(memberResponseDto.getProvider())
                .accessToken(accessToken)
                .refreshToken(refreshToken)
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
        String refreshToken = jwtProvider.buildRefreshToken(memberResponseDto.getProviderId(), memberResponseDto.getId());

        saveRefreshToken(memberResponseDto.getId(), refreshToken);

        return LoginResponseDto.builder()
                .provider(memberResponseDto.getProvider())
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    private void saveRefreshToken(Long memberId, String refreshToken) {
        Member member = memberLoginService.findMemberById(memberId);
        LocalDateTime expiresAt = LocalDateTime.now().plusSeconds(300); // 5분짜리 토큰

        memberRefreshTokenService.saveOrUpdateToken(member, refreshToken, expiresAt);
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

    public LoginResponseDto refreshAccessToken(RefreshTokenRequestDto requestDto) {
        String refreshToken = requestDto.getRefreshToken();

        // 유효한 refresh token인지 검증
        if (!jwtProvider.validateToken(refreshToken)) {
            throw new CustomException(ErrorCode.INVALID_REFRESH_TOKEN);
        }

        // DB에서 토큰 정보 조회
        MemberRefreshToken storedToken = memberRefreshTokenService.findByRefreshToken(refreshToken);

        // 유저 정보 추출
        Long memberId = jwtProvider.getMemberId(refreshToken);
        String providerId = jwtProvider.getProviderId(refreshToken);
        Member member = storedToken.getMember(); // or memberLoginService.findMemberById(memberId);

        // 새 AccessToken 발급
        String newAccessToken = jwtProvider.buildAccessToken(providerId, memberId);
        String newRefreshToken = jwtProvider.buildRefreshToken(providerId, memberId);

        // 새 refresh token 저장 (갱신)
        memberRefreshTokenService.saveOrUpdateToken(member, newRefreshToken, LocalDateTime.now().plusSeconds(300));

        return LoginResponseDto.builder()
                .provider(member.getProvider())
                .accessToken(newAccessToken)
                .refreshToken(newRefreshToken)
                .build();
    }


    // 멤버 로그아웃
    public MemberResponseDto logout(Long memberId){
        Member member = memberLoginService.findMemberById(memberId);
        memberRefreshTokenService.deleteByMember(member);

        log.info("ID: {} 인 사용자가 로그아웃 했습니다.", memberId);

        return MemberResponseDto.builder()
                .id(member.getId())
                .nickname(member.getNickname())
                .provider(member.getProvider())
                .providerId(member.getProviderId())
                .exploredSpot(member.getExploredSpot())
                .recordedSpot(member.getRecordedSpot())
                .userCharacterId(member.getLevelingUserCharacter() != null ? member.getLevelingUserCharacter().getId() : null)
                .eggId(member.getLevelingEgg() != null ? member.getLevelingEgg().getId() : null)
                .memberTier(member.getMemberTier())
                .isPublic(member.getIsPublic())
                .build();
    }

}
