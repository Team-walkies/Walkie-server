package site.walkies.walkie.domain.health.service.dto.request;

import lombok.Getter;

import java.time.LocalDate;

@Getter
public class HealthMoveUpdateRequestDto {
    private Integer targetSteps;
    private Integer nowSteps;
    private Double nowDistance;
    private Double nowCalories;
    private LocalDate nowDay;
}
