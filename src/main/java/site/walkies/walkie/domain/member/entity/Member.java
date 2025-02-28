package site.walkies.walkie.domain.member.entity;

import jakarta.persistence.*;
import lombok.Getter;
import site.walkies.walkie.domain.character.entity.UserCharacter;
import site.walkies.walkie.domain.egg.entity.Egg;

@Entity
@Table(name = "member")
@Getter
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "nickname")
    private String nickname;

    @Column(name = "password")
    private String password;

    @Column(name = "explored_spot")
    private Integer exploredSpot;

    @Column(name = "is_public")
    private Boolean isPublic;

    @Column(name = "member_tier")
    private String memberTier;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "leveling_egg_id")
    private Egg levelingEgg;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "leveling_character_id")
    private UserCharacter levelingUserCharacter;
}
