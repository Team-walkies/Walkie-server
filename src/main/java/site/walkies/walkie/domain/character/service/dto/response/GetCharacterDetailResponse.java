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
    // 캐릭터 타입
    private Integer type;
    // 캐릭터 클래스
    private Integer characterClass;
    // 획득 상세 정보
    private List<ObtainedDetail> obtainedDetails;
}
