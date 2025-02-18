package site.walkies.walkie.domain.character.entity;

import java.time.LocalDate;
import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Table(name = "character_born")
@Getter
public class CharacterBorn {

    @Id
    @Column(name = "id")
    private Long id;

    @Column(name = "obtained_date")
    private LocalDate obtainedDate;

    @Column(name = "obtained_position", columnDefinition = "MEDIUMTEXT")
    private String obtainedPosition;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "character_id", nullable = false)
    private Character character;
}