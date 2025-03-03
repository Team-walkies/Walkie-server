package site.walkies.walkie.domain.review.service.dto.response;

import site.walkies.walkie.domain.character.service.dto.response.GetCharacterResponse;

import java.util.List;

public class ReviewListResponse {
    private List<GetReviewResponse> reviews;

    public ReviewListResponse(List<GetReviewResponse> reviews) {
        this.reviews = reviews;
    }

    public List<GetReviewResponse> getReviews() {
        return reviews;
    }

    public void setReviews(List<GetReviewResponse> reviews) {
        this.reviews = reviews;
    }
}
