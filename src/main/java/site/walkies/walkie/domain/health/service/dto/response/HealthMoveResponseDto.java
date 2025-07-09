package site.walkies.walkie.domain.health.service.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class HealthMoveResponseDto {
    private Integer targetSteps;
    private Integer nowSteps;
    private Double nowDistance;
    private Double nowCalories;
}
