package site.walkies.walkie.domain.spot.service.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class SpotNearbyRequestDto {
    private double latitude;
    private double longitude;
}
