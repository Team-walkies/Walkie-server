package site.walkies.walkie.domain.support.service.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SupportRequestDto {
    // 내용
    private String detail;
    // 기기 및 부가 정보
    private String information;
}
