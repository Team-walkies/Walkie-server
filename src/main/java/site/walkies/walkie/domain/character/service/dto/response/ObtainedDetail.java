package site.walkies.walkie.domain.character.service.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@Builder
public class ObtainedDetail {
    // 얻은 위치
    private String obtainedPosition;
    // 얻은 날짜
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate obtainedDate;
}
