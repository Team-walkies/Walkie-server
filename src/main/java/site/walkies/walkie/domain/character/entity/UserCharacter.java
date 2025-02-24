package site.walkies.walkie.domain.character.entity;

import jakarta.persistence.*;
import lombok.Getter;
import site.walkies.walkie.domain.member.entity.Member;

@Entity
@Table(name = "user_character")
@Getter
public class UserCharacter {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "rank")
    private Integer rank;

    @Column(name = "type")
    private Integer type;

    // 'class'는 예약어이므로 다른 이름으로 매핑합니다.
    @Column(name = "class")
    private Integer characterClass;

    @Column(name = "picked")
    private Boolean picked;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private Member user;

    public UserCharacter(Integer rank, Integer type, Integer characterClass, Boolean picked, Member user) {
        this.rank = rank;
        this.type = type;
        this.characterClass = characterClass;
        this.picked = picked;
        this.user = user;
    }

    public UserCharacter() {

    }
}