package site.walkies.walkie.domain.review.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import site.walkies.walkie.domain.review.service.ReviewService;
import site.walkies.walkie.domain.review.service.dto.request.PatchReviewRequest;
import site.walkies.walkie.domain.review.service.dto.request.ReviewData;
import site.walkies.walkie.domain.review.service.dto.response.PatchReviewResponse;
import site.walkies.walkie.domain.review.service.dto.response.PostReviewResponse;
import site.walkies.walkie.global.web.dto.response.SuccessResponse;

@RestController
@RequestMapping("/reviews")
@RequiredArgsConstructor
public class ReviewController {
    private final ReviewService reviewService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public SuccessResponse<?> postReview(@RequestPart("reviewData") ReviewData reviewData, @RequestPart(value = "pic", required = false) MultipartFile pic) {
        PostReviewResponse response = reviewService.postReview(2,reviewData.getSpotId(),reviewData.getDistance(),reviewData.getStep(),reviewData.getDate(),reviewData.getStartTime(),reviewData.getEndTime(),reviewData.getCharacterId(),reviewData.getReviewCd(),pic,reviewData.getReview(),reviewData.getRating());
        return SuccessResponse.created(response);
    }
    
    @PatchMapping("/{reviewId}")
    public SuccessResponse<?> updateReview(@PathVariable("reviewId") Long reviewId, @RequestBody PatchReviewRequest patchReviewRequest) {
        PatchReviewResponse response = reviewService.patchReviewResponse(reviewId, patchReviewRequest.getReview(),patchReviewRequest.getRating());
        // 추후 update로 변경 필요
        return SuccessResponse.ok(response);
    }
}
