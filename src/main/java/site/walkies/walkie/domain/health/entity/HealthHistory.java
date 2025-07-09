package site.walkies.walkie.domain.health.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import site.walkies.walkie.domain.member.entity.Member;

import java.time.LocalDate;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "health_history")
public class HealthHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @Column(name = "record_date")
    private LocalDate recordDate;

    @Column(name = "target_steps")
    private Integer targetSteps;

    @Column(name = "day_steps")
    private Integer daySteps;

    @Column(name = "day_distance")
    private Double dayDistance;

    @Column(name = "day_calories")
    private Double dayCalories;

    @Column(name = "continuous_days")
    private Integer continuousDays;

    public HealthHistory(Member member, LocalDate recordDate, Integer targetSteps, Integer daySteps, Double dayDistance, Double dayCalories, Integer continuousDays) {
        this.member = member;
        this.recordDate = recordDate;
        this.targetSteps = targetSteps;
        this.daySteps = daySteps;
        this.dayDistance = dayDistance;
        this.dayCalories = dayCalories;
        this.continuousDays = continuousDays;
    }

    public static HealthHistory create(Member member, LocalDate recordDate, Integer targetSteps, Integer daySteps, Double dayDistance, Double dayCalories, Integer continuousDays) {
        return new HealthHistory(
                member,
                recordDate,
                targetSteps,
                daySteps,
                dayDistance,
                dayCalories,
                continuousDays
        );
    }
}
