package site.walkies.walkie.domain.character.service.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class GetCharacterCount {
    private Integer charactersCount;
}
