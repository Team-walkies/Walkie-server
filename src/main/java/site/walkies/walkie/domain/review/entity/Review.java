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
    @Column(name = "id")
    private Long id;

    @Column(name = "review", columnDefinition = "TEXT")
    private String reviewText;

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
    private String imageUrl;

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
}
