package site.walkies.walkie.domain.character.service.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class GetCharacterDetailResponse {
    // 캐릭터 갯수
    private Integer characterCount;
    // 캐릭터 등급
    private Integer rank;
    // 획득 상세 정보
    private List<ObtainedDetail> obtainedDetails;
}
