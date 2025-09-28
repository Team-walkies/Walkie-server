package site.walkies.walkie.domain.member.service.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@NoArgsConstructor
@Getter
public class MemberResponseDto {
    private Long id;
    private String providerId;
    private String provider;
    private String nickname;
    private Integer exploredSpot;
    private Integer recordedSpot;
    private Boolean isPublic;
    private String memberTier;
    private Long eggId;
    private Long userCharacterId;
    private LocalDate joinedAt;

    @Builder
    public MemberResponseDto(Long id, String providerId, String provider, String nickname, Integer exploredSpot, Integer recordedSpot, Boolean isPublic, String memberTier, Long eggId, Long userCharacterId, LocalDate joinedAt) {
        this.id = id;
        this.providerId = providerId;
        this.provider = provider;
        this.nickname = nickname;
        this.exploredSpot = exploredSpot;
        this.recordedSpot = recordedSpot;
        this.isPublic = isPublic;
        this.memberTier = memberTier;
        this.eggId = eggId;
        this.userCharacterId = userCharacterId;
        this.joinedAt = joinedAt;
    }
}
