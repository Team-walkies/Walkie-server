package site.walkies.walkie.domain.health.service.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@Builder
public class HealthResponseDto {
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate responseDate;
    private Integer targetSteps;
    private Integer nowSteps;
}
