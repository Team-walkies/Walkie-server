package site.walkies.walkie.domain.health.service.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class HealthContinueDayResponseDto {
    private Integer continuousDays;
}
