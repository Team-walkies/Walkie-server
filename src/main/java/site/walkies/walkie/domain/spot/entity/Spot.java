package site.walkies.walkie.domain.spot.entity;

import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Table(name = "spot")
@Getter
public class Spot {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "location_name")
    private String locationName;

    @Column(name = "latitude")
    private Double latitude;

    @Column(name = "longtitude")
    private Double longtitude;

    @Column(name = "street_address")
    private String streetAddress;

    @Column(name = "photo_url")
    private String photoUrl;

    @Column(name = "h3_index")
    private Long h3Index;
}
