package site.walkies.walkie.domain.health.service.dto.request;

import lombok.Getter;

@Getter
public class HealthMoveUpdateRequestDto {
    private Integer targetSteps;
    private Integer nowSteps;
    private Double nowDistance;
    private Double nowCalories;
}
