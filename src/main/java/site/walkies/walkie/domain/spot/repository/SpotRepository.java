package site.walkies.walkie.domain.spot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import site.walkies.walkie.domain.spot.entity.Spot;

import java.util.List;

@Repository
public interface SpotRepository extends JpaRepository<Spot, Long> {
    List<Spot> findByH3IndexIn(List<String> h3Indexes);
    Boolean existsByLocationName(String locationName);
    List<Spot> findByH3IndexIsNull();
}
