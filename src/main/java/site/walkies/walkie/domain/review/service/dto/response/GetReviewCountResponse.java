package site.walkies.walkie.domain.review.service.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class GetReviewCountResponse {
    private Integer count;
}
