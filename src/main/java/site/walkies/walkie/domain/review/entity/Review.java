package site.walkies.walkie.domain.review.entity;

import jakarta.persistence.*;
import lombok.Getter;
import site.walkies.walkie.domain.character.entity.UserCharacter;
import site.walkies.walkie.domain.member.entity.Member;
import site.walkies.walkie.domain.spot.entity.Spot;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "review")
@Getter
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "review", columnDefinition = "TEXT")
    private String review;

    @Column(name = "rating")
    private Double rating;

    @Column(name = "distance")
    private Double distance;

    @Column(name = "step")
    private Integer step;

    @Column(name = "review_date")
    private LocalDate reviewDate;

    @Column(name = "start_time")
    private LocalTime startTime;

    @Column(name = "end_time")
    private LocalTime endTime;

    @Column(name = "image_url", columnDefinition = "MEDIUMTEXT")
    private String pic;

    @Column(name = "review_cd")
    private Boolean reviewCd;

    @Column(name = "delete_cd")
    private Boolean deleteCd;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "spot_id", nullable = false)
    private Spot spot;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "character_id", nullable = false)
    private UserCharacter userCharacter;

    // 기본 생성자 (JPA용)
    protected Review() {
    }

    public static Review createReview(
            Member member,
            Spot spot,
            Double distance,
            Integer step,
            LocalDate reviewDate,
            LocalTime startTime,
            LocalTime endTime,
            UserCharacter userCharacter,
            String imageUrl,
            Boolean reviewCd,
            String reviewText,
            Double rating
    ) {
        Review review = new Review();
        review.review = reviewText;
        review.rating = rating;
        review.distance = distance;
        review.step = step;
        review.reviewDate = reviewDate;
        review.startTime = startTime;
        review.endTime = endTime;
        review.pic = imageUrl;
        review.reviewCd = reviewCd;
        review.deleteCd = false; // 기본값으로 false 지정
        review.member = member;
        review.spot = spot;
        review.userCharacter = userCharacter;
        return review;
    }

    public Review updateReviewCd(Boolean reviewCd) {
        this.reviewCd = reviewCd;
        return this;
    }

    public Review updateDeleteCd(Boolean deleteCd) {
        this.deleteCd = deleteCd;
        return this;
    }

    public Review updateReviewText(String reviewText) {
        this.review = reviewText;
        return this;
    }

    public Review updateRating(Double rating) {
        this.rating = rating;
        return this;
    }
}
