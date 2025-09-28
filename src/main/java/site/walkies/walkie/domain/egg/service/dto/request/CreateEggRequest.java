package site.walkies.walkie.domain.egg.service.dto.request;

import lombok.Getter;

import java.time.LocalDate;

@Getter
public class CreateEggRequest {
    // null 값 처리르 위해서 defualt 값 설정
    private Double latitude = -1d;
    private Double longitude = -1d;
    private LocalDate getHealthEggDate;
}
