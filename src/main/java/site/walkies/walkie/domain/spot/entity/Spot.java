package site.walkies.walkie.domain.spot.entity;

import jakarta.persistence.*;
import lombok.Getter;
import site.walkies.walkie.domain.spot.enums.SpotType;

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

    @Column(name = "longitude")
    private Double longitude;

    @Column(name = "street_address")
    private String streetAddress;

    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private SpotType type;

    @Column(name = "h3_index")
    private Long h3Index;
}
