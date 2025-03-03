package site.walkies.walkie.domain.egg.service.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@Builder
public class EggResponse {

    private Long eggId;

    private Integer rank;

    private Integer needStep;

    private Integer nowStep;

    private String obtainedPosition;

    private LocalDate obtainedDate;

    private Boolean picked;

    private Long userCharacterId;

    private Long memberId;
}
