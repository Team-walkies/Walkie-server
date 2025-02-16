package site.walkies.walkie.domain.character.entity;

import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Table(name = "character")
@Getter
public class Character {

    @Id
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

//    @OneToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "user_id", nullable = false)
//    private Member member;
}