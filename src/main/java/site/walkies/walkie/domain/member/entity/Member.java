package site.walkies.walkie.domain.member.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import site.walkies.walkie.domain.character.entity.UserCharacter;
import site.walkies.walkie.domain.egg.entity.Egg;

import java.time.LocalDateTime;

@Entity
@Table(name = "member")
@Getter
@NoArgsConstructor
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "providerId", nullable = false, unique = true)
    private String providerId;

    @Column(name = "provider", nullable = false)
    private String provider;

    @Column(name = "nickname")
    private String nickname;

    @Column(name = "explored_spot")
    private Integer exploredSpot;

    @Column(name = "recorded_spot")
    private Integer recordedSpot;

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

    @Column(name = "delete_cd")
    private Boolean deleteCd;

    @Column(name = "delete_requested_at")
    private LocalDateTime deleteRequestedAt;

    @Builder
    public Member(Long id, String providerId, String provider, String nickname, Integer exploredSpot, Integer recordedSpot, Boolean isPublic, String memberTier, Egg levelingEgg, UserCharacter levelingUserCharacter, Boolean deleteCd) {
        this.id = id;
        this.providerId = providerId;
        this.provider = provider;
        this.nickname = nickname;
        this.exploredSpot = exploredSpot;
        this.recordedSpot = recordedSpot;
        this.isPublic = isPublic;
        this.memberTier = memberTier;
        this.levelingEgg = levelingEgg;
        this.levelingUserCharacter = levelingUserCharacter;
        this.deleteCd = deleteCd;
    }

    public void changeNickname(String newNickname) {
        this.nickname = newNickname;
    }

    public void changeLevelingUserCharacter(UserCharacter newLevelingUserCharacter) {
        this.levelingUserCharacter = newLevelingUserCharacter;
    }

    public void changeLevelingEgg(Egg newLevelingEgg) {
        this.levelingEgg = newLevelingEgg;
    }

    public void changeProfileVisibility(Boolean isPublic){
        this.isPublic = isPublic;
    }

    public void markDeleted(LocalDateTime now) {
        this.deleteCd = true;
        this.deleteRequestedAt = now;
    }

    public void increaseExploredSpot() {
        this.exploredSpot = this.exploredSpot == null ? 1 : this.exploredSpot + 1;
    }

    public void increaseRecordedSpot() {
        this.recordedSpot = this.recordedSpot == null ? 1 : this.recordedSpot + 1;
    }

    public void decreaseRecordedSpot() {
        this.recordedSpot = this.recordedSpot == null ? 0 : this.recordedSpot - 1;
    }
}
