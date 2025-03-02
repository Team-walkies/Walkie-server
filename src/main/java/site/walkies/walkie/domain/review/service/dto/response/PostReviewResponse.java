package site.walkies.walkie.domain.review.service.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Builder
public class PostReviewResponse {
    private Long spotId;
    private Double distance;
    private Integer step;
    private LocalDate date;
    private LocalTime startTime;
    private LocalTime endTime;
    private Long characterId;
    private Boolean reviewCd;
    private String pic;
    private String review;
    private Double rating;
}
