package site.walkies.walkie.domain.review.service.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Builder
public class GetReviewResponse {
    private Long reviewId;

    private Long spotId;
    // 스팟 이름 추가
    private String spotName;

    private Double distance;
    private Integer step;
    private LocalDate date;
    private LocalTime startTime;
    private LocalTime endTime;

    private Long characterId;
    // 캐릭터 상세 정보 추가
    private Integer rank;
    private Integer type;
    private Integer characterClass;

    private String pic;
    private Boolean reviewCd;
    private String review;
    private Double rating;
}
