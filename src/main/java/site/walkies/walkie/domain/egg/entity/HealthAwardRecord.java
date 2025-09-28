package site.walkies.walkie.domain.egg.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import site.walkies.walkie.domain.member.entity.Member;

import java.time.LocalDate;

@Entity
@Table(name = "health_award_record")
@Getter
@NoArgsConstructor
public class HealthAwardRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @Column(name = "received_date")
    private LocalDate receivedDate;

    public HealthAwardRecord(Member member, LocalDate receivedDate) {
        this.member = member;
        this.receivedDate = receivedDate;
    }
}
