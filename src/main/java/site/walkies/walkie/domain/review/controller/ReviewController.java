package site.walkies.walkie.domain.review.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import site.walkies.walkie.domain.review.service.ReviewService;
import site.walkies.walkie.domain.review.service.dto.request.PatchReviewRequest;
import site.walkies.walkie.domain.review.service.dto.request.ReviewData;
import site.walkies.walkie.domain.review.service.dto.response.GetReviewCountResponse;
import site.walkies.walkie.domain.review.service.dto.response.PatchReviewResponse;
import site.walkies.walkie.domain.review.service.dto.response.PostReviewResponse;
import site.walkies.walkie.domain.review.service.dto.response.ReviewListResponse;
import site.walkies.walkie.global.web.dto.response.SuccessResponse;

import java.time.LocalDate;
import java.util.Map;

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

    @GetMapping("/calendar")
    public SuccessResponse<ReviewListResponse> getReviewListByCalendar(@RequestParam(value = "startDate", defaultValue = "2000-01-01")LocalDate startDate, @RequestParam(value = "endDate", defaultValue = "9999-12-31")LocalDate endDate) {
        ReviewListResponse response = reviewService.getReviewList(2,startDate,endDate);
        return SuccessResponse.ok(response);
    }

    @GetMapping("/spots")
    public SuccessResponse<ReviewListResponse> getReviewListBySpots(@RequestParam("spotId") long spotId) {
        ReviewListResponse response = reviewService.getReviewList(2,spotId);
        return SuccessResponse.ok(response);
    }

    @PatchMapping("/{reviewId}")
    public SuccessResponse<?> updateReview(@PathVariable("reviewId") Long reviewId, @RequestBody PatchReviewRequest patchReviewRequest) {
        PatchReviewResponse response = reviewService.patchReviewResponse(reviewId, patchReviewRequest.getReview(),patchReviewRequest.getRating());
        // 추후 update로 변경 필요
        return SuccessResponse.ok(response);
    }


    @DeleteMapping("/{reviewId}")
    public SuccessResponse<?> deleteReview(@PathVariable("reviewId") Long reviewId) {
        long id = reviewService.deleteReview(reviewId);
        // 추후 delete로 변경 필요
        return SuccessResponse.ok(id);
    }
  
    @GetMapping("/count/{spotId}")
    public SuccessResponse<GetReviewCountResponse> getReviewCount(@PathVariable("spotId") long spotId) {
        GetReviewCountResponse response = reviewService.getReviewCount(spotId);
        return SuccessResponse.ok(response);
    }
}
