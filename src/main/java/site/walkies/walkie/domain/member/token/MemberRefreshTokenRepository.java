package site.walkies.walkie.domain.member.token;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import site.walkies.walkie.domain.member.entity.Member;

import java.util.Optional;

@Repository
public interface MemberRefreshTokenRepository extends JpaRepository<MemberRefreshToken, Long> {

    // member 기반으로 refresh token 조회
    Optional<MemberRefreshToken> findByMember(Member member);

    // 실제 refreshToken 문자열로 조회 (토큰 재발급 시 사용)
    Optional<MemberRefreshToken> findByRefreshToken(String refreshToken);
}
