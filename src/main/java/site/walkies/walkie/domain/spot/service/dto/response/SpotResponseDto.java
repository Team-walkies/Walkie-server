package site.walkies.walkie.domain.spot.service.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class SpotResponseDto {
    private Long id;
    private String locationName;
    private Double latitude;
    private Double longitude;
    private String streetAddress;
    private String type;
    private List<String> photoUrls;
    private boolean isExplored;
    private int daysUntilNextVisit;
    private int visitCount;
    private int reviewCount;

    @Builder
    public SpotResponseDto(Long id, String locationName, Double latitude, Double longitude, String streetAddress, String type, List<String> photoUrls, boolean isExplored, int daysUntilNextVisit, int visitCount, int reviewCount) {
        this.id = id;
        this.locationName = locationName;
        this.latitude = latitude;
        this.longitude = longitude;
        this.streetAddress = streetAddress;
        this.type = type;
        this.photoUrls = photoUrls;
        this.isExplored = isExplored;
        this.daysUntilNextVisit = daysUntilNextVisit;
        this.visitCount = visitCount;
        this.reviewCount = reviewCount;
    }
}

