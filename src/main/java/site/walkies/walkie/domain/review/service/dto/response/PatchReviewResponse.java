package site.walkies.walkie.domain.review.service.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Builder
public class PatchReviewResponse {
    private Long reviewId;
    private Long spotId;
    private Double distance;
    private Integer step;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate date;
    @JsonFormat(pattern = "HH:mm:ss")
    private LocalTime startTime;
    @JsonFormat(pattern = "HH:mm:ss")
    private LocalTime endTime;
    private Long characterId;
    private Boolean reviewCd;
    private String pic;
    private String review;
    private Double rating;
}
