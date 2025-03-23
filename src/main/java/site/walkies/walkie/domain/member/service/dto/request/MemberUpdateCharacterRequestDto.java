package site.walkies.walkie.domain.member.service.dto.request;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class MemberUpdateCharacterRequestDto {
    private Long characterId;

    @Builder
    public MemberUpdateCharacterRequestDto(Long characterId) {
        this.characterId = characterId;
    }
}
