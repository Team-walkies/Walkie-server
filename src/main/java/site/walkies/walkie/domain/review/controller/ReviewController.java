package site.walkies.walkie.domain.review.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import site.walkies.walkie.domain.review.service.ReviewService;
import site.walkies.walkie.domain.review.service.dto.request.PatchReviewRequest;
import site.walkies.walkie.domain.review.service.dto.request.ReviewData;
import site.walkies.walkie.domain.review.service.dto.response.GetReviewCountResponse;
import site.walkies.walkie.domain.review.service.dto.response.PatchReviewResponse;
import site.walkies.walkie.domain.review.service.dto.response.PostReviewResponse;
import site.walkies.walkie.domain.review.service.dto.response.ReviewListResponse;
import site.walkies.walkie.global.auth.dto.MemberPrincipal;
import site.walkies.walkie.global.web.dto.response.SuccessResponse;

import java.time.LocalDate;
import java.util.Map;

@RestController
@RequestMapping("/reviews")
@RequiredArgsConstructor
public class ReviewController {
    private final ReviewService reviewService;

    @Operation(
            summary = "리뷰 작성",
            description = "걷기 기록 및 리뷰를 작성합니다. 텍스트 데이터와 함께 이미지 파일(pic)을 multipart/form-data 형식으로 전송해야 합니다."
    )
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public SuccessResponse<?> postReview(@RequestPart("reviewData") ReviewData reviewData, @RequestPart(value = "pic", required = false) MultipartFile pic,@AuthenticationPrincipal MemberPrincipal memberPrincipal) {
        PostReviewResponse response = reviewService.postReview(memberPrincipal.getMemberId(),reviewData.getSpotId(),reviewData.getDistance(),reviewData.getStep(),reviewData.getDate(),reviewData.getStartTime(),reviewData.getEndTime(),reviewData.getCharacterId(),reviewData.getReviewCd(),pic,reviewData.getReview(),reviewData.getRating());
        return SuccessResponse.created(response);
    }

    @Operation(
            summary = "리뷰 목록 조회 (기간 필터)",
            description = "선택한 시작일과 종료일 사이의 리뷰를 조회합니다. 쿼리 파라미터로 startDate, endDate를 전달하며, 기본값은 전체 기간입니다."
    )
    @GetMapping("/calendar")
    public SuccessResponse<ReviewListResponse> getReviewListByCalendar(@RequestParam(value = "startDate", defaultValue = "2000-01-01")LocalDate startDate, @RequestParam(value = "endDate", defaultValue = "9999-12-31")LocalDate endDate, @AuthenticationPrincipal MemberPrincipal memberPrincipal) {
        ReviewListResponse response = reviewService.getReviewList(memberPrincipal.getMemberId(),startDate,endDate);
        return SuccessResponse.ok(response);
    }

    @Operation(
            summary = "리뷰 목록 조회 (스팟 기반)",
            description = "특정 스팟에 해당하는 리뷰 목록을 조회합니다."
    )
    @GetMapping("/spots")
    public SuccessResponse<ReviewListResponse> getReviewListBySpots(@RequestParam("spotId") long spotId, @AuthenticationPrincipal MemberPrincipal memberPrincipal) {
        ReviewListResponse response = reviewService.getReviewList(memberPrincipal.getMemberId(),spotId);
        return SuccessResponse.ok(response);
    }

    @Operation(
            summary = "리뷰 수정",
            description = "작성된 리뷰의 내용과 평점을 수정합니다."
    )
    @PatchMapping("/{reviewId}")
    public SuccessResponse<?> updateReview(@PathVariable("reviewId") Long reviewId, @RequestBody PatchReviewRequest patchReviewRequest) {
        PatchReviewResponse response = reviewService.patchReviewResponse(reviewId, patchReviewRequest.getReview(),patchReviewRequest.getRating());
        return SuccessResponse.updated(response);
    }

    @Operation(
            summary = "리뷰 삭제",
            description = "특정 리뷰를 삭제합니다. "
    )
    @DeleteMapping("/{reviewId}")
    public SuccessResponse<?> deleteReview(@PathVariable("reviewId") Long reviewId) {
        long id = reviewService.deleteReview(reviewId);
        return SuccessResponse.deleted(id);
    }

    @Operation(
            summary = "스팟 리뷰 개수 조회",
            description = "특정 스팟에 등록된 리뷰의 총 개수를 조회합니다."
    )
    @GetMapping("/count/{spotId}")
    public SuccessResponse<GetReviewCountResponse> getReviewCount(@PathVariable("spotId") long spotId) {
        GetReviewCountResponse response = reviewService.getReviewCount(spotId);
        return SuccessResponse.ok(response);
    }
}
