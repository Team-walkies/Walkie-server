package site.walkies.walkie.domain.egg.service.dto.response;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import site.walkies.walkie.domain.character.entity.UserCharacter;
import site.walkies.walkie.domain.member.entity.Member;

import java.time.LocalDate;

@Getter
@Builder
public class PostEggResponse {

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
