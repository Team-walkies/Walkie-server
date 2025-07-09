package site.walkies.walkie.domain.health.service.dto.response;

import lombok.Builder;
import lombok.Getter;
import site.walkies.walkie.domain.member.entity.Member;

@Getter
@Builder
public class HealthDetailResponseDto {
    private Integer targetSteps;
    private Integer nowSteps;
    private Double nowDistance;
    private Double nowCalories;
    private String caloriesName;
    private String caloriesDescription;
    private String caloriesUrl;
}
