package site.walkies.walkie.domain.spot.entity;

import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Table(name = "spot_photo")
@Getter
public class SpotPhoto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "spot_id")
    private Spot spot;

    @Column(name = "photo_url", columnDefinition = "TEXT")
    private String photoUrl;
}

