package site.walkies.walkie.domain.egg.service.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class GetEggCountResponse {
    private Integer eggCount;
}
