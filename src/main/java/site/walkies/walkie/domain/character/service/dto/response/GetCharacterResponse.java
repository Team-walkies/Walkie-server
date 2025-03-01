package site.walkies.walkie.domain.character.service.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class GetCharacterResponse {
    //characterId
    private Long characterId;
    // 캐릭터 타입
    private Integer type;
    // 캐릭터 클래스
    private Integer characterClass;
    // 캐릭터 등급
    private Integer rank;
    // 캐릭터 개수
    private Integer count;
    // 같이 걷는 여부
    private Boolean picked;
}
