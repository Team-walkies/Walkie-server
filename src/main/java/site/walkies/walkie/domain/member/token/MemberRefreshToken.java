package site.walkies.walkie.domain.member.token;

import jakarta.persistence.*;
import lombok.*;
import site.walkies.walkie.domain.member.entity.Member;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class MemberRefreshToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Member와 1:1 연관관계 (Unique)
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false, unique = true)
    private Member member;

    // 저장된 Refresh Token
    @Column(nullable = false, length = 512)
    private String refreshToken;

    // 토큰 만료 시각
    @Column(nullable = false)
    private LocalDateTime expiresAt;

    // 토큰 갱신 시 사용
    public void updateToken(String refreshToken, LocalDateTime expiresAt) {
        this.refreshToken = refreshToken;
        this.expiresAt = expiresAt;
    }

    // 만료 여부 확인
    public boolean isExpired() {
        return LocalDateTime.now().isAfter(this.expiresAt);
    }
}
