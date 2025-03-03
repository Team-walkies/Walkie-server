package site.walkies.walkie.domain.egg.service.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PatchEggResponse {
    // egg ID
    private Long eggId;
    // egg 등급
    private Integer rank;
    //부화까지 필요한 걸음 수(초기에 필요한 걸음 수)
    private Integer needStep;
    //현재 걸음 수
    private Integer nowStep;
    //부화할 character ID
    private Long characterId;
    //같이 걷는 알 여부
    private Boolean play;
}
