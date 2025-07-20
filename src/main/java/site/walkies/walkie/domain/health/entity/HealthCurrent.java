package site.walkies.walkie.domain.health.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import site.walkies.walkie.domain.member.entity.Member;

import java.time.LocalDate;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "health_current")
public class HealthCurrent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @Column(name = "target_steps")
    private Integer targetSteps;

    @Column(name = "now_steps")
    private Integer nowSteps;

    @Column(name = "now_distance")
    private Double nowDistance;

    @Column(name = "now_calories")
    private Double nowCalories;

    @Column(name = "now_day")
    private LocalDate nowDay;

    public HealthCurrent(Member member, Integer targetSteps, Integer nowSteps, Double nowDistance, Double nowCalories, LocalDate nowDay) {
        this.member = member;
        this.targetSteps = targetSteps;
        this.nowSteps = nowSteps;
        this.nowDistance = nowDistance;
        this.nowCalories = nowCalories;
        this.nowDay = nowDay;
    }

    public void updateMove(Integer nowSteps, Double nowDistance, Double nowCalories) {
        this.nowSteps = nowSteps;
        this.nowDistance = nowDistance;
        this.nowCalories = nowCalories;
    }

    public void updateTargetSteps(Integer targetSteps) {
        this.targetSteps = targetSteps;
    }

    public static HealthCurrent create(Member member, Integer targetSteps, Integer nowSteps, Double nowDistance, Double nowCalories, LocalDate nowDay) {
        return new HealthCurrent(
                member,
                targetSteps,
                nowSteps,
                nowDistance,
                nowCalories,
                nowDay
        );
    }
}
