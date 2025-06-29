// DailyEggEventResponse.java
package site.walkies.walkie.domain.event.service.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class DailyEggEventResponse {
    private boolean receivedToday; // 오늘 알을 받았는지 여부
    private int remainingDays;     // 7월 31일 기준으로 남은 D-day
}
