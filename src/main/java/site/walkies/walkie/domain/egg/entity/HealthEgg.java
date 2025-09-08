package site.walkies.walkie.domain.egg.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import site.walkies.walkie.domain.member.entity.Member;

import java.time.LocalDate;

@Entity
@Table(name = "health_egg")
@Getter
@NoArgsConstructor
public class HealthEgg {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "member_id", nullable = false, unique = true)
    private Member member;

    @Column(name = "last_received_date")
    private LocalDate lastReceivedDate;

    public HealthEgg(Member member, LocalDate lastReceivedDate) {
        this.member = member;
        this.lastReceivedDate = lastReceivedDate;
    }

    public void updateLastReceivedDate(LocalDate date) {
        this.lastReceivedDate = date;
    }
}
