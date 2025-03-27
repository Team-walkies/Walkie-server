package site.walkies.walkie.domain.spot.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.walkies.walkie.domain.review.entity.Review;
import site.walkies.walkie.domain.review.repository.ReviewRepository;
import site.walkies.walkie.domain.spot.entity.Spot;
import site.walkies.walkie.domain.spot.entity.SpotPhoto;
import site.walkies.walkie.domain.spot.repository.SpotPhotoRepository;
import site.walkies.walkie.domain.spot.repository.SpotRepository;
import site.walkies.walkie.domain.spot.service.dto.response.SpotResponseDto;
import site.walkies.walkie.global.web.exception.CustomException;
import site.walkies.walkie.global.web.exception.ErrorCode;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class SpotService {

    private final SpotRepository spotRepository;
    private final SpotPhotoRepository spotPhotoRepository;
    private final ReviewRepository reviewRepository;

    public SpotResponseDto getSpotInfo(Long spotId, Long memberId) {
        Spot spot = spotRepository.findById(spotId)
                .orElseThrow(() -> new CustomException(ErrorCode.SPOT_NOT_FOUND));

        // 사진 URL 리스트 조회
        List<String> photoUrls = spotPhotoRepository.findBySpotId(spotId).stream()
                .map(SpotPhoto::getPhotoUrl)
                .toList();

        // 해당 유저의 해당 스팟에 대한 리뷰
        List<Review> memberReviews = reviewRepository.findBySpotIdAndMemberIdOrderByReviewDateDesc(spotId, memberId);

        // 탐험 여부
        boolean isExplored = !memberReviews.isEmpty();

        // 재탐험까지 남은 일수
        int daysUntilNextVisit = 0;
        if (isExplored) {
            LocalDate lastReviewDate = memberReviews.get(0).getReviewDate();
            long daysPassed = ChronoUnit.DAYS.between(lastReviewDate, LocalDate.now());
            daysUntilNextVisit = (daysPassed >= 3) ? 0 : (int)(3 - daysPassed);
        }

        // 리뷰 수
        int reviewCount = reviewRepository.countBySpotIdAndDeleteCdFalse(spotId);

        // 방문자 수 (distinct member 기준)
        int visitCount = reviewRepository.countDistinctBySpotIdAndDeleteCdFalse(spotId);

        // DTO 생성
        return SpotResponseDto.builder()
                .id(spot.getId())
                .locationName(spot.getLocationName())
                .latitude(spot.getLatitude())
                .longitude(spot.getLongitude())
                .streetAddress(spot.getStreetAddress())
                .type(spot.getType().name()) // enum -> String
                .photoUrls(photoUrls)
                .isExplored(isExplored)
                .daysUntilNextVisit(daysUntilNextVisit)
                .visitCount(visitCount)
                .reviewCount(reviewCount)
                .h3Index(spot.getH3Index())
                .build();
    }

}
