package site.walkies.walkie.domain.review.service.dto.request;

import lombok.Getter;

@Getter
public class PatchReviewRequest {
    private String review;
    private Double rating;
}
