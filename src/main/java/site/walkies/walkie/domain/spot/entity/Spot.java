package site.walkies.walkie.domain.spot.entity;

import jakarta.persistence.*;
import lombok.*;
import site.walkies.walkie.domain.spot.enums.SpotKeyword;

@Entity
@Table(name = "spot")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
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
    private SpotKeyword keyword;

    @Column(name = "h3_index")
    private String h3Index;

    @Column(name = "detail_url", columnDefinition = "longtext")
    @Setter
    private String detailUrl;

    public void changeKeyword(SpotKeyword spotKeyword) {
        this.keyword = spotKeyword;
    }

    public void changeDetailUrl(String detailUrl) {
        this.detailUrl = detailUrl;
    }

    public void changeStreetAddress(String streetAddress) {
        this.streetAddress = streetAddress;
    }
}
