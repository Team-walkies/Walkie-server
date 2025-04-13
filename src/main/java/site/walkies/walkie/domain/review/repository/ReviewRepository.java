package site.walkies.walkie.domain.review.repository;

import org.springframework.cglib.core.Local;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import site.walkies.walkie.domain.review.entity.Review;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
    // start와 end 사이의 기간 조회
    List<Review> findByMemberIdAndAndReviewDateBetween(Long id, LocalDate start, LocalDate end);

    // spotId로 조회
    List<Review> findByMemberIdAndSpotId(Long id, Long spotId);

    // 리뷰 갯수 조회
    int countBySpotId(Long spotId);

    // 해당 유저의 해당 스팟에 대한 리뷰 날짜 내림차순 조회
    List<Review> findBySpotIdAndMemberIdOrderByReviewDateDesc(Long spotId, Long memberId);

    // 스팟에 대한 방문자 수 조회
    int countDistinctBySpotIdAndDeleteCdFalse(Long spotId);

    // 스팟에 대한 전체 리뷰 수 조회
    int countBySpotIdAndDeleteCdFalse(Long spotId);

    // 삭제를 위한 userId를 통한 조회
    List<Review> findByMemberId(Long memberId);
}
