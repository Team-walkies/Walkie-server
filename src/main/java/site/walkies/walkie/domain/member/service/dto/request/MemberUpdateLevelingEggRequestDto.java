package site.walkies.walkie.domain.member.service.dto.request;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class MemberUpdateLevelingEggRequestDto {
    private Long eggId;

    @Builder
    public MemberUpdateLevelingEggRequestDto(Long eggId) {
        this.eggId = eggId;
    }
}
