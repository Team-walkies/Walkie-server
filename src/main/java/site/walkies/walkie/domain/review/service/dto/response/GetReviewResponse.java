package site.walkies.walkie.domain.review.service.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Getter;
import site.walkies.walkie.domain.spot.enums.SpotKeyword;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Builder
public class GetReviewResponse {
    private Long reviewId;

    private Long spotId;
    // 스팟 이름 추가
    private String spotName;
    // 스팟 키워드 추가
    private SpotKeyword keyword;

    private String memberNickname;

    private Double distance;
    private Integer step;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate date;
    @JsonFormat(pattern = "HH:mm:ss")
    private LocalTime startTime;
    @JsonFormat(pattern = "HH:mm:ss")
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
