package site.walkies.walkie.domain.spot.service;

import com.uber.h3core.H3Core;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.walkies.walkie.domain.review.entity.Review;
import site.walkies.walkie.domain.review.repository.ReviewRepository;
import site.walkies.walkie.domain.spot.entity.Spot;
import site.walkies.walkie.domain.spot.entity.SpotPhoto;
import site.walkies.walkie.domain.spot.repository.SpotPhotoRepository;
import site.walkies.walkie.domain.spot.repository.SpotRepository;
import site.walkies.walkie.domain.spot.service.dto.request.SpotNearbyRequestDto;
import site.walkies.walkie.domain.spot.service.dto.response.SpotNearbyResponseDto;
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
    private final H3Core h3Core;

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
                .build();
    }

    public List<SpotNearbyResponseDto> getNearbySpots(SpotNearbyRequestDto request, Long memberId) {
        int resolution = 9;
        int k = 30;

        String userH3 = h3Core.geoToH3Address(request.getLatitude(), request.getLongitude(), resolution);
        // System.out.println("User H3: " + userH3);

        List<String> h3Rings = h3Core.kRing(userH3, k);
        // System.out.println("H3 Ring Count: " + h3Rings.size());
        // System.out.println("H3 Ring Sample: " + h3Rings.subList(0, Math.min(5, h3Rings.size())));

        // List<Spot> allSpots = spotRepository.findAll();
        // System.out.println("All Spot h3Indexes:");
        // allSpots.forEach(s -> System.out.println("  " + s.getLocationName() + " → " + s.getH3Index()));

        List<Spot> nearbySpots = spotRepository.findByH3IndexIn(h3Rings);
        // System.out.println("Found Spots: " + nearbySpots.size());

        // String h3FromRequest = h3Core.geoToH3Address(37.5639, 126.9873, 9);
        // System.out.println("명동성당 직접 계산한 H3: " + h3FromRequest);

        return nearbySpots.stream()
                .map(spot -> SpotNearbyResponseDto.builder()
                        .id(spot.getId())
                        .locationName(spot.getLocationName())
                        .type(isVisitedSpot(spot.getId(), memberId) ? spot.getType().name() : spot.getType().name()+"_VISITED")
                        .latitude(spot.getLatitude())
                        .longitude(spot.getLongitude())
                        .build())
                .toList();
    }

    private boolean isVisitedSpot(Long nearSpotId, Long memberId){
        return reviewRepository.countByMemberIdAndSpotIdAndDeleteCdFalse(memberId ,nearSpotId) > 0;
    }
}
