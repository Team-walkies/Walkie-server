package site.walkies.walkie.domain.egg.service.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@Builder
public class GetEggResponse {
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
    // 얻은 위치
    private String obtainedPosition;
    // 얻은 날짜
    private LocalDate obtainedDate;

    // 생성 함수
    public static GetEggResponse createGetEggResponse(Long eggId, Integer rank, Integer needStep, Integer nowStep, Long characterId, Boolean play, String obtainedPosition, LocalDate obtainedDate) {
        return GetEggResponse.builder()
                .eggId(eggId)
                .rank(rank)
                .needStep(needStep)
                .nowStep(nowStep)
                .characterId(characterId)
                .play(play)
                .obtainedPosition(obtainedPosition)
                .obtainedDate(obtainedDate)
                .build();
    }
}
