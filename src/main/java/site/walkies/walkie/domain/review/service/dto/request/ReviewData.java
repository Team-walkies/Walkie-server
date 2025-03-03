package site.walkies.walkie.domain.review.service.dto.request;

import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
public class ReviewData {
    private Long spotId;
    private Double distance;
    private Integer step;
    private LocalDate date;
    private LocalTime startTime;
    private LocalTime endTime;
    private Long characterId;
    private Boolean reviewCd;
    private String review;
    private Double rating;
}
