package site.walkies.walkie.domain.egg.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.*;
import lombok.*;
import site.walkies.walkie.domain.member.entity.Member;

import java.time.LocalDate;

@Entity
@Table(name = "egg")
@Getter
public class Egg {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "rank")
    private Integer rank;

    @Column(name = "need_step")
    private Integer needStep;

    @Column(name = "now_step")
    private Integer nowStep;

    @Column(name = "obtained_position")
    private String obtainedPosition;

    @Column(name = "obtained_date")
    private LocalDate obtainedDate;

    @Column(name = "picked")
    private Boolean picked;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id", nullable = false)
    private Member owner;
}
