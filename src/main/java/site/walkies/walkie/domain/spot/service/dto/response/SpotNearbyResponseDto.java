package site.walkies.walkie.domain.spot.service.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class SpotNearbyResponseDto {
    private Long id;
    private String locationName;
    private String type;
    private Double latitude;
    private Double longitude;

    @Builder
    public SpotNearbyResponseDto(Long id, String locationName, String type, Double latitude, Double longitude) {
        this.id = id;
        this.locationName = locationName;
        this.type = type;
        this.latitude = latitude;
        this.longitude = longitude;
    }
}

