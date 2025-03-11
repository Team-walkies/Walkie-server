package site.walkies.walkie.global.auth.dto;

import lombok.Builder;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import site.walkies.walkie.domain.member.entity.Member;

import java.util.Collection;
import java.util.List;

@Getter
public class MemberPrincipal implements UserDetails {

    private Long memberId;
    private String providerId;
    private String provider;

    public static MemberPrincipal createMemberAuthority(Member member){
        return MemberPrincipal.builder()
                .memberId(member.getId())
                .providerId(member.getProviderId())
                .provider(member.getProvider())
                .build();
    }

    @Builder
    private MemberPrincipal(Long memberId, String providerId, String provider) {
        this.memberId = memberId;
        this.providerId = providerId;
        this.provider = provider;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }

    @Override
    public String getPassword() {
        return null; // 소셜 로그인 방식이므로 비밀번호 사용 안 함
    }

    @Override
    public String getUsername() {
        return providerId; // 소셜 로그인이므로 고유한 식별자인 providerId 반환
    }
}
