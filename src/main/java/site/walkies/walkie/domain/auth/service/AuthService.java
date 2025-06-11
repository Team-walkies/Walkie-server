package site.walkies.walkie.domain.auth.service;

import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import site.walkies.walkie.domain.auth.service.dto.request.AuthCheckRequestDto;
import site.walkies.walkie.domain.auth.service.dto.request.AuthSignupRequestDto;
import site.walkies.walkie.domain.auth.service.dto.request.RefreshTokenRequestDto;
import site.walkies.walkie.domain.auth.service.dto.response.LoginResponseDto;
import site.walkies.walkie.domain.auth.strategy.SocialLoginStrategy;
import site.walkies.walkie.domain.member.entity.Member;
import site.walkies.walkie.domain.member.service.MemberLoginService;
import site.walkies.walkie.domain.member.service.dto.response.MemberResponseDto;
import site.walkies.walkie.domain.member.token.MemberRefreshToken;
import site.walkies.walkie.domain.member.token.MemberRefreshTokenService;
import site.walkies.walkie.global.auth.utils.JWTProvider;
import site.walkies.walkie.global.web.exception.CustomException;
import site.walkies.walkie.global.web.exception.ErrorCode;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
public class AuthService {

    private final MemberLoginService memberLoginService;
    private final JWTProvider jwtProvider;
    private final MemberRefreshTokenService memberRefreshTokenService;
    private final Map<String, SocialLoginStrategy> strategyMap;

    // 전략 매핑을 위하여 `@RequiredArgsConstructor` 대신 생성자 주입 사용
    public AuthService(
            List<SocialLoginStrategy> strategies,
            JWTProvider jwtProvider,
            MemberRefreshTokenService memberRefreshTokenService,
            MemberLoginService memberLoginService
    ){
        this.jwtProvider = jwtProvider;
        this.memberRefreshTokenService = memberRefreshTokenService;
        this.memberLoginService = memberLoginService;
        this.strategyMap = strategies.stream().collect(Collectors.toMap(SocialLoginStrategy::getProviderName, s -> s));
    }

    // 사용자가 있다면 -> 로그인, 없다면 -> 회원가입 유도
    public LoginResponseDto checkUserExists(AuthCheckRequestDto requestDto) {
        SocialLoginStrategy strategy = getStrategy(requestDto.getProvider());

        MemberResponseDto memberResponseDto;
        try{
            memberResponseDto = strategy.findMember(requestDto.getLoginAccessToken());
        } catch (CustomException e) {
            if(e.getErrorCode() == ErrorCode.USER_NOT_FOUND) {
                // 회원이 없는 경우: 프론트에서 회원가입 유도
                return LoginResponseDto.builder()
                        .provider(requestDto.getProvider())
                        .accessToken(null)
                        .refreshToken(null)
                        .build();
            }
            throw e;
        }

        // 토큰 발급
        String accessToken = jwtProvider.buildAccessToken(memberResponseDto.getProviderId(), memberResponseDto.getId());
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

    // 사용자 회원가입
    public LoginResponseDto signupNewUser(AuthSignupRequestDto requestDto) {
        SocialLoginStrategy strategy = getStrategy(requestDto.getProvider());

        MemberResponseDto memberResponseDto = strategy.signup(
                requestDto.getLoginAccessToken(),
                requestDto.getNickname()
        );

        String accessToken = jwtProvider.buildAccessToken(memberResponseDto.getProviderId(), memberResponseDto.getId());
        String refreshToken = jwtProvider.buildRefreshToken(memberResponseDto.getProviderId(), memberResponseDto.getId());

        saveRefreshToken(memberResponseDto.getId(), refreshToken);

        return LoginResponseDto.builder()
                .provider(memberResponseDto.getProvider())
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    // RefreshToken 저장
    private void saveRefreshToken(Long memberId, String refreshToken) {
        Member member = memberLoginService.findMemberById(memberId);
        LocalDateTime expiresAt = LocalDateTime.now().plusSeconds(300); // 5분짜리 토큰

        memberRefreshTokenService.saveOrUpdateToken(member, refreshToken, expiresAt);
    }

    // 리프레시 토큰 재발급
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

    // provider에 알맞은 전략 조회
    private SocialLoginStrategy getStrategy(String provider) {
        SocialLoginStrategy strategy = strategyMap.get(provider.toLowerCase());
        if (strategy == null) {
            throw new IllegalArgumentException("지원하지 않는 로그인 방식입니다: " + provider);
        }
        return strategy;
    }

}
