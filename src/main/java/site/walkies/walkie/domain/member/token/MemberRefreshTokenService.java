package site.walkies.walkie.domain.member.token;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.walkies.walkie.domain.member.entity.Member;
import site.walkies.walkie.global.web.exception.CustomException;
import site.walkies.walkie.global.web.exception.ErrorCode;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberRefreshTokenService {

    private final MemberRefreshTokenRepository tokenRepository;

    /**
     * 회원별로 RefreshToken을 저장하거나 갱신합니다.
     */
    @Transactional
    public void saveOrUpdateToken(Member member, String refreshToken, LocalDateTime expiresAt) {
        tokenRepository.findByMember(member)
                .ifPresentOrElse(
                        existingToken -> existingToken.updateToken(refreshToken, expiresAt),
                        () -> tokenRepository.save(MemberRefreshToken.builder()
                                .member(member)
                                .refreshToken(refreshToken)
                                .expiresAt(expiresAt)
                                .build())
                );
    }

    /**
     * RefreshToken 값으로 해당 객체를 찾습니다.
     */
    public MemberRefreshToken findByRefreshToken(String refreshToken) {
        return tokenRepository.findByRefreshToken(refreshToken)
                .orElseThrow(() -> new CustomException(ErrorCode.INVALID_REFRESH_TOKEN));
    }

    /**
     * 로그아웃 등 시 RefreshToken 제거
     */
    @Transactional
    public void deleteByMember(Member member) {
        tokenRepository.findByMember(member)
                .ifPresent(tokenRepository::delete);
    }
}
