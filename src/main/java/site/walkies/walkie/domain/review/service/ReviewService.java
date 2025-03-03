package site.walkies.walkie.domain.review.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import site.walkies.walkie.domain.character.entity.UserCharacter;
import site.walkies.walkie.domain.character.repository.UserCharacterRepository;
import site.walkies.walkie.domain.member.entity.Member;
import site.walkies.walkie.domain.member.repository.MemberRepository;
import site.walkies.walkie.domain.review.entity.Review;
import site.walkies.walkie.domain.review.repository.ReviewRepository;
import site.walkies.walkie.domain.review.service.dto.response.*;
import site.walkies.walkie.domain.spot.entity.Spot;
import site.walkies.walkie.domain.spot.repository.SpotRepository;
import site.walkies.walkie.global.file.FileService;
import site.walkies.walkie.global.web.exception.CustomException;
import site.walkies.walkie.global.web.exception.ErrorCode;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final MemberRepository memberRepository;
    private final SpotRepository spotRepository;
    private final UserCharacterRepository userCharacterRepository;

    private final FileService fileService;

    // 리뷰 작성 method
    // input : userId, spotId (스팟 id), distance (걸은 거리), step(걸은 걸음), date(걸은 날짜), startTime(시작 시간), endTime(종료 시간), characterId(캐릭터 id), pic(사진), reviewCd(리뷰 작성 여부),  review(리뷰), rating(평점)
    // output: PostReviewResponse
    public PostReviewResponse postReview(long userId, long spotId, double distance, int step, LocalDate date, LocalTime startTime, LocalTime endTime, long characterId, boolean reviewCd, MultipartFile pic, String review, double rating) {
        Member member = memberRepository.findById(userId).orElse(null);
        if (member == null) {
            throw new CustomException(ErrorCode.USER_NOT_FOUND);
        }

        Spot spot = spotRepository.findById(spotId).orElse(null);
        if (spot == null) {
            return null;
        }

        UserCharacter userCharacter = userCharacterRepository.findById(characterId).orElse(null);
        if (userCharacter == null) {
            throw new CustomException(ErrorCode.CHARACTER_NOT_FOUND);
        }

        // 파일 저장
        String fileName = fileService.saveFile(pic);

        // 리뷰 생성
        Review createReview = Review.createReview(member,spot,distance,step,date,startTime,endTime,userCharacter,fileName,reviewCd,review,rating);

        //리뷰 저장
        reviewRepository.save(createReview);

        PostReviewResponse response = PostReviewResponse.builder()
                .spotId(createReview.getSpot().getId())
                .distance(createReview.getDistance())
                .step(createReview.getStep())
                .date(createReview.getReviewDate())
                .startTime(createReview.getStartTime())
                .endTime(createReview.getEndTime())
                .characterId(createReview.getUserCharacter().getId())
                .reviewCd(createReview.getReviewCd())
                .pic(createReview.getPic())
                .review(createReview.getReview())
                .rating(createReview.getRating())
                .build();

        return response;
    }

    // 캘린더 리뷰 리스트 조회 method
    // input : userId, startDate(조회 시작 날짜), endDate(조회 종료 날짜)
    // output : ReviewListResponse
    public ReviewListResponse getReviewList(long userId, LocalDate startDate, LocalDate endDate) {
        Member member = memberRepository.findById(userId).orElse(null);
        if (member == null) {
            throw new CustomException(ErrorCode.USER_NOT_FOUND);
        }

        List<Review> reviews =  reviewRepository.findByMemberIdAndAndReviewDateBetween(userId,startDate,endDate);

        // 조회된 리뷰를 저장할 리스트
        List<GetReviewResponse> responses = new ArrayList<>();
        for (Review review : reviews) {
            GetReviewResponse getReviewResponse = GetReviewResponse.builder()
                    .reviewId(review.getId())
                    .spotId(review.getSpot().getId())
                    .distance(review.getDistance())
                    .step(review.getStep())
                    .date(review.getReviewDate())
                    .startTime(review.getStartTime())
                    .endTime(review.getEndTime())
                    .characterId(review.getUserCharacter().getId())
                    .pic(review.getPic())
                    .reviewCd(review.getReviewCd())
                    .review(review.getReview())
                    .rating(review.getRating())
                    .build();
            responses.add(getReviewResponse);
        }

        ReviewListResponse response = new ReviewListResponse(responses);
        return response;
    }

    // 스팟의 리뷰 리스트 조회 method
    // input : userId, spotId
    // output : ReviewListResponse
    public ReviewListResponse getReviewList(long userId, long spotId) {
        Member member = memberRepository.findById(userId).orElse(null);
        if (member == null) {
            throw new CustomException(ErrorCode.USER_NOT_FOUND);
        }

        List<Review> reviews =  reviewRepository.findByMemberIdAndSpotId(userId,spotId);

        // 조회된 리뷰를 저장할 리스트
        List<GetReviewResponse> responses = new ArrayList<>();
        for (Review review : reviews) {
            GetReviewResponse getReviewResponse = GetReviewResponse.builder()
                    .reviewId(review.getId())
                    .spotId(review.getSpot().getId())
                    .distance(review.getDistance())
                    .step(review.getStep())
                    .date(review.getReviewDate())
                    .startTime(review.getStartTime())
                    .endTime(review.getEndTime())
                    .characterId(review.getUserCharacter().getId())
                    .pic(review.getPic())
                    .reviewCd(review.getReviewCd())
                    .review(review.getReview())
                    .rating(review.getRating())
                    .build();
            responses.add(getReviewResponse);
        }

        ReviewListResponse response = new ReviewListResponse(responses);
      
        return response;
    }
  
    // 리뷰 수정 method
    // input : reviewId, review, rating
    // output : PatchReviewResponse
    public PatchReviewResponse patchReviewResponse(long reviewId, String review, double rating) {
        Review patchReview = reviewRepository.findById(reviewId).orElse(null);
        if (review == null) {
            throw new CustomException(ErrorCode.REVIEW_NOT_FOUND);
        }
        patchReview.updateReviewCd(true).updateReviewText(review).updateRating(rating);

        Review patchedReview = reviewRepository.save(patchReview);

        PatchReviewResponse response = PatchReviewResponse.builder()
                .reviewId(patchedReview.getId())
                .spotId(patchedReview.getSpot().getId())
                .distance(patchedReview.getDistance())
                .step(patchedReview.getStep())
                .date(patchedReview.getReviewDate())
                .startTime(patchedReview.getStartTime())
                .endTime(patchedReview.getEndTime())
                .characterId(patchedReview.getUserCharacter().getId())
                .pic(patchedReview.getPic())
                .reviewCd(patchedReview.getReviewCd())
                .review(patchedReview.getReview())
                .rating(patchedReview.getRating())
                .build();

        return response;
    }

    // 리뷰 삭제 method
    // input : reviewId
    // output : reviewId
    public long deleteReview(long reviewId) {
        Review review = reviewRepository.findById(reviewId).orElse(null);
        if (review == null) {
            throw new CustomException(ErrorCode.REVIEW_NOT_FOUND);
        }
        reviewRepository.delete(review);
        return review.getId();
    }

    // 스팟 별 리뷰수 조회 method
    // input : userId, spotId
    // output : GetReviewCount
    public GetReviewCountResponse getReviewCount(long spotId) {
        int count = reviewRepository.countBySpotId(spotId);

        GetReviewCountResponse response = GetReviewCountResponse.builder()
                .count(count)
                .build();

        return response;
    }
}
