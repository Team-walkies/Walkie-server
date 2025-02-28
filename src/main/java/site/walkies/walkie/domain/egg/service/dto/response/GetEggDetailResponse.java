package site.walkies.walkie.domain.egg.service.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@Builder
public class GetEggDetailResponse {
    // 알 등급
    private Integer rank;
    // 부화에 필요한 걸음 수
    private Integer needStep;
    // 현재 걸음 수
    private Integer nowStep;
    // 얻은 위치
    private String obtainedPosition;
    // 얻은 날짜
    private LocalDate obtainedDate;
}
