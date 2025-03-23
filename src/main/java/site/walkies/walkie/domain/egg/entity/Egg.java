package site.walkies.walkie.domain.egg.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.*;
import lombok.*;
import site.walkies.walkie.domain.character.entity.UserCharacter;
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

    @Column(name = "`rank`")
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
    @JoinColumn(name = "character_id", nullable = false)
    private UserCharacter userCharacter;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private Member user;

    public Egg() {

    }

    public Egg(Integer rank, Integer needStep, Integer nowStep, String obtainedPosition, LocalDate obtainedDate, Boolean picked, UserCharacter userCharacter, Member user) {
        this.rank = rank;
        this.needStep = needStep;
        this.nowStep = nowStep;
        this.obtainedPosition = obtainedPosition;
        this.obtainedDate = obtainedDate;
        this.picked = picked;
        this.userCharacter = userCharacter;
        this.user = user;
    }

    public Egg eggNowStepUpdate(Integer nowStep) {
        this.nowStep = nowStep;
        return this;
    }

    public Egg changePicked(Boolean picked) {
        this.picked = picked;
        return this;
    }
}
